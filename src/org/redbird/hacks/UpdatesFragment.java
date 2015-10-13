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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

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
				R.color.darkred, R.color.darkred, R.color.white);
		return rootView;
	}

	/**
	 * TO DO: Use the Jackson library to parse JSON.
	 * 
	 * @author MJ
	 * 
	 */
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
			
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httppost = new HttpPost(url[0]);

			httppost.setHeader("Content-type", "application/json");

			InputStream inputStream = null;
			String jsonString = null;
			try {
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line = null;

				// Read each line of the JSON and build it into a String.
				while ((line = reader.readLine()) != null) {
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

				// For each update within the updates JSONArray, grab the
				// text and the date.

				for (int i = 0; i < updates.length(); i++) {
					JSONObject a = updates.getJSONObject(i);
					long updatesTime = a.getLong(TAG_UPDATES_DATE);

					if (i == 0)
						newestUpdateTime = a.getLong(TAG_UPDATES_DATE);

					// Convert epoch time to a Date.
					// Unix epoch time is measured in seconds. Multiply by 1000
					// for milliseconds
					cal.setTimeInMillis(updatesTime * 1000);
					String updatesDate = DateUtils.getRelativeDateTimeString(
							getActivity().getBaseContext(),
							cal.getTimeInMillis(), DateUtils.SECOND_IN_MILLIS,
							DateUtils.WEEK_IN_MILLIS,
							DateUtils.FORMAT_SHOW_TIME).toString();

					updatesInfo.updatesText[i] = a.getString(TAG_UPDATES_TEXT);
					updatesInfo.updatesDate[i] = updatesDate;
				}
			} catch (Exception e) {
				// The connection to the server failed. Throw a flag so that we
				// can
				// catch it in onPostExecute().
				connectionFailed = true;
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (Exception squish) {
				}
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

		// Get the JSON updates each time the fragment is resumed.
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