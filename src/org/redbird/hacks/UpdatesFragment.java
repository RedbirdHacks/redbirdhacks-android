/**
 * This fragment gets called when the app is initialized.
 * 
 * It references the custom ListView inside updates_fragment_layout.xml and adds items to the list.
 * The list is dynamically populated by parsing JSON Objects from the RedBirdHacks website.
 * 
 * @author MJ Havens <me@mjhavens.com>
 */

package org.redbird.hacks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class UpdatesFragment extends Fragment
{
	private final String url = "http://mjhavens.com/announcements.json";
	private ListView	listViewUpdates;
	private  List<Updates> legendList;
	View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
        // Inflate the layout for this fragment
    	rootView = inflater.inflate(R.layout.updates_fragment_layout, container, false);
    	
    	// Get the JSON announcements in the background and set each announcement to a ListItem.
    	new GetJSONUpdates().execute(url);

        return rootView;
    }
	
	private class GetJSONUpdates extends AsyncTask<String, Void, UpdatesInfoFromJSON> {
		private UpdatesInfoFromJSON updatesInfo;
		private final String TAG_ANNOUNCEMENTS = "announcements";
		private final String TAG_ANNOUNCEMENT_TEXT = "text";
		private final String TAG_ANNOUNCEMENT_DATE = "date";

		@Override
		protected UpdatesInfoFromJSON doInBackground(String... url) {
			DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
	    	HttpPost httppost = new HttpPost(url[0]);
	    	// Depends on your web service
	    	httppost.setHeader("Content-type", "application/json");

	    	InputStream inputStream = null;
	    	String jsonString = null;
	    	try {
	    	    HttpResponse response = httpclient.execute(httppost);           
	    	    HttpEntity entity = response.getEntity();

	    	    inputStream = entity.getContent();
	    	    // json is UTF-8 by default
	    	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
	    	    StringBuilder sb = new StringBuilder();

	    	    String line = null;

	    	    // Read each line of the JSON and build it into a String.
	    	    while ((line = reader.readLine()) != null)
	    	    {
	    	        sb.append(line + "\n");
	    	    }
	    	    jsonString = sb.toString();
	    	    
	    	    // Convert the result String to a JSONObject.
				JSONObject jObject = new JSONObject(jsonString);
				
				// Announcements is an array that contains each individual update. 
				// Set the "announcements" tag to a JSONArray
				
				JSONArray announcements = jObject.getJSONArray(TAG_ANNOUNCEMENTS);
				updatesInfo = new UpdatesInfoFromJSON();
				updatesInfo.updatesText = new String[announcements.length()];
				updatesInfo.updatesDate = new String[announcements.length()];
				
				for(int i=0; i < announcements.length(); i++)
				{
					// For each announcement within the announcements array, grab the text and the date.
					JSONObject a = announcements.getJSONObject(i);
					updatesInfo.updatesText[i] = a.getString(TAG_ANNOUNCEMENT_TEXT);
					updatesInfo.updatesDate[i] = a.getString(TAG_ANNOUNCEMENT_DATE);

				}
	    	} catch (Exception e) { 
	    	    e.printStackTrace();
	    	}
	    	finally {
	    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	    	}
			return updatesInfo;
		
		}
		 @Override
	     protected void onPostExecute(UpdatesInfoFromJSON updatesInfo) {
	         super.onPostExecute(updatesInfo);
			 // This executes after the doInBackground() method is finished.
			 // It will set all of the announcement text and dates to an item in the ListView.
	         
			 listViewUpdates = ( ListView ) rootView.findViewById( R.id.updatesFeed_List);
	         legendList = new ArrayList<Updates>();

	         // For every announcement...
	         for(int i=0; i < updatesInfo.updatesText.length; i++)
	         {
	        	 // Add the text and date to the ListView.
	        	legendList.add(new Updates(updatesInfo.updatesText[i],updatesInfo.updatesDate[i]));
	         }

	         listViewUpdates.setAdapter( new UpdatesListAdapter(getActivity(), R.layout.updates_listview, legendList ) );
	         listViewUpdates.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                    Updates o = (Updates) parent.getItemAtPosition(position); 
	                    Toast.makeText(getActivity(), o.getText().toString(), Toast.LENGTH_SHORT).show();
	                    }
	            });    
		 }
		 
		
	}
	
	/**
	 * This class was created so that we could pass both the 
	 * update text and update date as an array to the onPostExecute() method of the AsyncTask.
	 * 
	 * @author MJ Havens <me@mjhavens.com>
	 *
	 */
	public class UpdatesInfoFromJSON{
		String updatesText[];
		String updatesDate[];
	}
    
}
