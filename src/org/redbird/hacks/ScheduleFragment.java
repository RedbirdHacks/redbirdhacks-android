package org.redbird.hacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.redbird.hacks.SimpleSectionAdapter.Sectionizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
			eventsList.addAll(buildStubbedScheduleEvent());
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

	private List<ScheduleEvent> buildStubbedScheduleEvent() {
		List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
		//TODO -- Absolutely need to store the times in a sortable way where 8PM comes after 9AM
		events.add(new ScheduleEvent("Registration", "", "8:00-9:00", "February 23rd"));
		events.add(new ScheduleEvent("Registration", "", "6:00-6:30", "February 23rd"));
		events.add(new ScheduleEvent("Registration", "", "4:30-7:00", "February 22nd"));

		return events;
	}
}