/**
*  ScheduleAdapter.java
*  RedbirdHacks
*
*  Created by Andrew Erickson.
*  Copyright (c) 2014 Andrew Erickson. All rights reserved.
**/
package org.redbird.hacks;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScheduleAdapter extends ArrayAdapter<ScheduleEvent> {

	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public ScheduleAdapter(Context ctx, int resourceId,
			List<ScheduleEvent> objects) {
		super(ctx, resourceId, objects);
		resource = resourceId; //
		inflater = LayoutInflater.from(ctx);
		context = ctx;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);

		ScheduleEvent scheduleEvent = getItem(position);
		TextView tvScheduleEventTitle = (TextView) convertView.findViewById(R.id.tvEventTitle);
		tvScheduleEventTitle.setText(scheduleEvent.getEventTitle());

		TextView tvScheduleEventTime = (TextView) convertView.findViewById(R.id.tvEventTime);
		tvScheduleEventTime.setText(scheduleEvent.getEventTime());

		return convertView;
	}
}