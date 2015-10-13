/**
 *  ScheduleFragment.java
 *  RedbirdHacks
 *
 *  Created by Andrew Erickson on 11/1/14.
 **/
package org.redbird.hacks;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.redbird.hacks.SimpleSectionAdapter.Sectionizer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScheduleFragment extends android.support.v4.app.ListFragment {

	private View fragmentView;

	private List<ScheduleEvent> eventsList = new ArrayList<ScheduleEvent>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.schdule_fragment_layout,
				container, false);

		return fragmentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (eventsList.isEmpty()) {
			try {
				eventsList.addAll(new RetrieveEvents().execute().get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Collections.sort(eventsList);

		ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity(),
				R.layout.schedule_list_item, eventsList);

		ScheduleSectionizer scheduleSectionizer = new ScheduleSectionizer();
		SimpleSectionAdapter<ScheduleEvent> sectionAdapter = new SimpleSectionAdapter<ScheduleEvent>(
				getActivity(), scheduleAdapter,
				R.layout.schedule_event_day_header, R.id.tvScheduleEventDay,
				scheduleSectionizer);
		setListAdapter(sectionAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ScheduleEvent o = (ScheduleEvent) l.getItemAtPosition(position);
		Toast.makeText(getActivity(), o.getEventDescription().toString(),
				Toast.LENGTH_SHORT).show();
	}

	private class ScheduleSectionizer implements Sectionizer<ScheduleEvent> {

		public ScheduleSectionizer() {
		}

		@Override
		public String getSectionTitleForItem(ScheduleEvent instance) {
			return instance.getEventDate();
		}
	}

	private class RetrieveEvents extends
			AsyncTask<Void, Void, List<ScheduleEvent>> {
		private final String TAG_EVENTS = "events";
		private boolean connectionFailed;
		private SimpleDateFormat fromTime_date_format = new SimpleDateFormat(
				"h:mm");
		private SimpleDateFormat toTime_date_format = new SimpleDateFormat(
				"h:mm");
		private Calendar cal = Calendar.getInstance();

		@Override 
		protected List<ScheduleEvent> doInBackground(Void... params) {
			List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
			final String url = "http://redbirdhacks.org/json/events.json";
			connectionFailed = false;
			
			Long from = null, to = null;
			String title = null, description = null;

			//Using the Jackson library to parse through JSON
			
			// Resources:
			// http://www.mkyong.com/java/jackson-streaming-api-to-read-and-write-json/
			// http://wiki.fasterxml.com/JacksonInFiveMinutes
			// http://wiki.fasterxml.com/JacksonStreamingApi
			// http://mark.gg/2013/05/26/tips-for-android-json-parsing-using-jackson/
			// https://capdroid.wordpress.com/2012/07/13/android-simple-json-parsing-using-jackson-apis/			
			// https://github.com/FasterXML/jackson-databind
			// http://www.studytrails.com/java/json/java-jackson-Serialization-list.jsp
			ObjectMapper mapper = new ObjectMapper(); 
			
			//so that it doesn't choke on the "eventDate" variable in ScheduleEvent
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			try {
				URL json = new URL(url);
				JsonNode node = mapper.readTree(json);
				node = node.get(TAG_EVENTS);
				
				TypeReference<List<ScheduleEvent>> typeRef = new TypeReference<List<ScheduleEvent>>(){};
				List<ScheduleEvent> eventList = mapper.readValue(node.traverse(), typeRef);//new URL(url), type.constructCollectionType(List.class, ScheduleEvent.class));
				
				for(ScheduleEvent e : eventList){
					from = Long.parseLong(e.getFromTime());
					to = Long.parseLong(e.getToTime());
					title = e.getEventTitle();
					description = e.getEventDescription();
					
					// Convert epoch time to a Date.
					// Unix epoch time is measured in seconds. Multiply by 1000
					// for milliseconds
					cal.setTimeInMillis(from * 1000);
					String month = cal.getDisplayName(Calendar.MONTH, 2,
							Locale.US);
					String dayOfMonth = String.valueOf(cal
							.get(Calendar.DAY_OF_MONTH));

					// Get the from time format and convert from milliseconds to
					// a time format.
					String fromTime = fromTime_date_format
							.format(cal.getTime());

					// Get the to time and convert from milliseconds to a time
					// format.
					cal.setTimeInMillis(to * 1000);
					String toTime = toTime_date_format.format(cal.getTime());

					// Add the event to the event list.
					events.add(new ScheduleEvent(title, description, fromTime,
							toTime, month + " " + dayOfMonth));
				}
			} catch (Exception e) { 
				connectionFailed = true;
				e.printStackTrace();
			}
			
			if (connectionFailed)
				events.add(new ScheduleEvent("Could not connect to the server",
						"Please check your connection", "", "", ""));

			return events;
		}
	}
}