package org.redbird.hacks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redbird.hacks.SimpleSectionAdapter.Sectionizer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleFragment extends android.support.v4.app.ListFragment {
	
	
	private View fragmentView;
	
	private List<ScheduleEvent> eventsList = new ArrayList<ScheduleEvent>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		fragmentView = inflater.inflate(R.layout.schdule_fragment_layout, container, false);
		
		return fragmentView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(eventsList.isEmpty()){
			try
			{
				eventsList.addAll(new RetrieveEvents().execute().get());
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Collections.sort(eventsList);

		ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity(), R.layout.schedule_list_item, eventsList );
		
		ScheduleSectionizer scheduleSectionizer = new ScheduleSectionizer();
		SimpleSectionAdapter<ScheduleEvent> sectionAdapter = new SimpleSectionAdapter<ScheduleEvent>(getActivity(), scheduleAdapter, R.layout.schedule_event_day_header, R.id.tvScheduleEventDay, scheduleSectionizer);
		setListAdapter(sectionAdapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		 ScheduleEvent o = (ScheduleEvent) l.getItemAtPosition(position); 
         Toast.makeText(getActivity(), o.getEventTitle().toString(), Toast.LENGTH_SHORT).show();
	}
	
	private class ScheduleSectionizer implements Sectionizer<ScheduleEvent>{
		
		public ScheduleSectionizer(){}

		@Override
		public String getSectionTitleForItem(ScheduleEvent instance) {
			return instance.getEventDate();
		}
	}
	
	private class RetrieveEvents extends AsyncTask<Void, Void, List<ScheduleEvent>>
	{
		private final String	TAG_EVENTS		= "events";
		private final String	TAG_DATE		= "date";
		private final String	TAG_TIME	= "time";
		private final String	TAG_TITLE		= "title";
		private final String	TAG_DESCRIPTION	= "description";
		private boolean			connectionFailed;

		@Override
		protected List<ScheduleEvent> doInBackground(Void... params)
		{
			List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
			final String url = "http://mjhavens.com/events.json";
			connectionFailed = false;
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httppost = new HttpPost(url);

			httppost.setHeader("Content-type", "application/json");

			InputStream inputStream = null;
			String jsonString = null;
			try
			{
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
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
				JSONArray eventsArray = jObject.getJSONArray(TAG_EVENTS);
				
				// Grab each mentor.
				for (int i = 0; i < eventsArray.length(); i++)
				{
					JSONObject a = eventsArray.getJSONObject(i);
					Long date = a.getLong(TAG_DATE);
					String time = a.getString(TAG_TIME);
					String title = a.getString(TAG_TITLE);					
					String description = a.getString(TAG_DESCRIPTION);

					//Convert epoch time to a Date.
					Calendar cal = Calendar.getInstance();
					//Unix epoch time is measured in seconds. Multiply by 1000 for milliseconds
					cal.setTimeInMillis(date * 1000);
					Log.d("APP", "Long: " + cal.get(Calendar.DAY_OF_MONTH));
					String month = cal.getDisplayName(Calendar.MONTH, 2, Locale.US);
					String dayOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					
					//Add the event to the event list.
					events.add(new ScheduleEvent(title, description, time, month + " " + dayOfMonth));
				}
			}
			catch (Exception e)
			{
				// The connection to the server failed. Throw a flag so that we can catch it later.
				connectionFailed = true;
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (inputStream != null)
						inputStream.close();
				}
				catch (Exception squish)
				{
				}
			}
			if(connectionFailed)
				events.add(new ScheduleEvent("Could not connect to the server", "Please check your connection", "", ""));
			
			return events;

		}
	}
}