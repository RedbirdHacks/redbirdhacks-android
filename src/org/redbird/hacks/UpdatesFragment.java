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
    	
    	// Get the JSON updates in the background and set each updates to a ListItem.
    	new GetJSONUpdates().execute(url);

        return rootView;
    }
	
	private class GetJSONUpdates extends AsyncTask<String, Void, UpdatesInfoFromJSON> {
		private UpdatesInfoFromJSON updatesInfo;
		private final String TAG_UPDATES = "announcements";
		private final String TAG_UPDATES_TEXT = "text";
		private final String TAG_UPDATES_DATE = "date";
		private boolean connectionFailed;

		@Override
		protected UpdatesInfoFromJSON doInBackground(String... url) {
			connectionFailed = false;
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
				
				// Updates is an array that contains each individual update. 
				// Set the "updates" tag to a JSONArray
				
				JSONArray updates = jObject.getJSONArray(TAG_UPDATES);
				updatesInfo = new UpdatesInfoFromJSON();
				updatesInfo.updatesText = new String[updates.length()];
				updatesInfo.updatesDate = new String[updates.length()];
				
				for(int i=0; i < updates.length(); i++)
				{
					// For each update within the updates JSONArray, grab the text and the date.
					JSONObject a = updates.getJSONObject(i);
					updatesInfo.updatesText[i] = a.getString(TAG_UPDATES_TEXT);
					updatesInfo.updatesDate[i] = a.getString(TAG_UPDATES_DATE);

				}
	    	} catch (Exception e) { 
	    		connectionFailed = true;
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
			 // It will set all of the update text and dates to an item in the ListView.
	         
			 listViewUpdates = ( ListView ) rootView.findViewById( R.id.updatesFeed_List);
	         legendList = new ArrayList<Updates>();

	         // Make sure that we were able to connect to the server.
	         if(!connectionFailed)
	         {
		         // For every update...
		         for(int i=0; i < updatesInfo.updatesText.length; i++)
		         {
		        	 // Add the text and date to the ListView.
		        	legendList.add(new Updates(updatesInfo.updatesText[i],updatesInfo.updatesDate[i]));
		         }
	         }
	         else
		        	legendList.add(new Updates("Unable to connect to the server!","Please check your internet connection."));
	         
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
