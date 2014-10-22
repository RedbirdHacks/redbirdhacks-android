package org.redbird.hacks;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScheduleFragment extends android.support.v4.app.Fragment {
	
	
	private View fragmentView;
	private ListView scheduleEventsListView;
	
	private List<ScheduleEvent> eventsList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		fragmentView = inflater.inflate(R.layout.schdule_fragment_layout, container, false);
		scheduleEventsListView = (ListView) fragmentView.findViewById(R.id.scheduleEventsList);
		
		eventsList = new ArrayList<ScheduleEvent>();
		eventsList.add(buildStubbedScheduleEvent());
		eventsList.add(buildStubbedScheduleEvent());

		
		scheduleEventsListView.setAdapter( new ScheduleAdapter(getActivity(), R.layout.schedule_list_item, eventsList ) );
	   
		scheduleEventsListView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ScheduleEvent o = (ScheduleEvent) parent.getItemAtPosition(position); 
	                Toast.makeText(getActivity(), o.getEventTitle().toString(), Toast.LENGTH_SHORT).show();
	                }
	        });      
		
		return fragmentView;
	}

	private ScheduleEvent buildStubbedScheduleEvent() {

		ScheduleEvent event = new ScheduleEvent();
		event.setEventTitle("Registration");
		event.setEventTime("8:00-9:00");
		return event;
	}
}