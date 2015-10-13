/**
 *  UpdatesFragment.java
 *  RedbirdHacks
 *
 * * This fragment gets called when the app is initialized.
 * 
 * It references the custom ListView inside updates_fragment_layout.xml and adds items to the list.
 * The list is dynamically populated by parsing JSON Objects from the RedBirdHacks website.
 * 
 *  Created by MJ Havens on 10/18/14.
 **/
package org.redbird.hacks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class UpdatesFragment extends Fragment implements OnRefreshListener {
	private final String url = "http://redbirdhacks.org/json/announcements.json";
	private ListView listViewUpdates;
	private List<Updates> legendList;
	private View rootView;
	private UpdatesListAdapter updatesListAdapter;
	private SwipeRefreshLayout swipeLayout;
	private long newestUpdateTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.updates_fragment_layout,
				container, false);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(R.color.redBirdHacksRed,
				R.color.orange, R.color.darkred, R.color.white);
		return rootView;
	}

	private class JSONUpdates extends
			AsyncTask<String, Void, UpdatesInfoFromJSON> {
		private UpdatesInfoFromJSON updatesInfo;
		private final String TAG_UPDATES = "announcements";
		private final String TAG_UPDATES_TEXT = "text";
		private final String TAG_UPDATES_DATE = "date";
		private boolean connectionFailed;
		private Calendar cal = Calendar.getInstance();
		private SimpleDateFormat updates_date_format = new SimpleDateFormat(
				"MMMM d, yyyy h:mm a");

		@Override
		protected UpdatesInfoFromJSON doInBackground(String... url) {
			connectionFailed = false;
			
			ObjectMapper mapper = new ObjectMapper(); 
			
			//so that it doesn't choke on the "eventDate" variable in ScheduleEvent
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			try {
				URL json = new URL(url[0]);
				JsonNode node = mapper.readTree(json);
				node = node.get(TAG_UPDATES);
				
				TypeReference<List<Updates>> typeRef = new TypeReference<List<Updates>>(){};
				List<Updates> updatesList = mapper.readValue(node.traverse(), typeRef);
				
				updatesInfo = new UpdatesInfoFromJSON();
				updatesInfo.updatesText = new String[updatesList.size()];
				updatesInfo.updatesDate = new String[updatesList.size()];
				
				for(int i = 0; i < updatesList.size(); i++){
					Updates u = updatesList.get(i);
					
					long updatesTime = Long.parseLong(u.getDate());
					String updatesText = u.getText();
					
					// Convert epoch time to a Date.
					// Unix epoch time is measured in seconds. Multiply by 1000
					// for milliseconds
					cal.setTimeInMillis(updatesTime * 1000);
					String updatesDate = DateUtils.getRelativeDateTimeString(
							getActivity().getBaseContext(),
							cal.getTimeInMillis(), DateUtils.SECOND_IN_MILLIS,
							DateUtils.WEEK_IN_MILLIS,
							DateUtils.FORMAT_SHOW_TIME).toString();
					
					updatesInfo.updatesText[i] = updatesText;
					updatesInfo.updatesDate[i] = updatesDate;
				}
			} catch (Exception e) {
				connectionFailed = true;
				e.printStackTrace();
			}
			
			return updatesInfo;

		}

		@Override
		protected void onPostExecute(UpdatesInfoFromJSON updatesInfo) {
			super.onPostExecute(updatesInfo);
			// This executes after the doInBackground() method is finished.
			// It will set all of the update text and dates to a view in the
			// ListView.

			listViewUpdates = (ListView) rootView
					.findViewById(R.id.updatesFeed_List);

			// Make sure that we were able to connect to the server.
			if (!connectionFailed) {
				// For every update...
				for (int i = 0; i < updatesInfo.updatesText.length; i++) {
					// Add the text and date to the ListView.
					legendList.add(new Updates(updatesInfo.updatesText[i],
							updatesInfo.updatesDate[i]));
				}
				Log.d("APP", "Sending broadcast");
				Intent i = new Intent("com.redbird.hacks.MESSAGE");
				i.putExtra("newestTime", newestUpdateTime);
				getActivity().sendBroadcast(i);
			} else
				legendList.add(new Updates("Unable to connect to the server!",
						"Please check your internet connection."));

			listViewUpdates.setAdapter(updatesListAdapter);
		}

	}

	/**
	 * This class was created so that we could pass both the update text and
	 * update date as an array to the onPostExecute() method of the AsyncTask.
	 * 
	 * @author MJ Havens <me@mjhavens.com>
	 * 
	 */ 
	private class UpdatesInfoFromJSON {
		String updatesText[];
		String updatesDate[];
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * Gets called right before the fragment becomes active. This is how the
	 * list view initially gets the updates.
	 */
	@Override
	public void onResume() {
		super.onResume();

		// Get the JSON updates in the background and set each update to a
		// ListItem.
		getUpdates();

	}

	/**
	 * This method will refresh the list view with the latest updates.
	 */
	private void getUpdates() {
		swipeLayout.setRefreshing(true);
		Log.d("APP", "Refreshing the adapter");
		legendList = new ArrayList<Updates>();
		updatesListAdapter = new UpdatesListAdapter(getActivity(),
				R.layout.updates_listview, legendList);
		new JSONUpdates().execute(url);
		updatesListAdapter.notifyDataSetChanged();
		swipeLayout.setRefreshing(false);

	}

	/**
	 * A listener that is called when a user swipes down on the screen.
	 */
	@Override
	public void onRefresh() {
		Log.d("APP", "Refreshed");
		getUpdates();
	}

}
