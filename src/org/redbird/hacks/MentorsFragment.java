package org.redbird.hacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MentorsFragment extends Fragment {
	
	private View fragmentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.mentors_fragment_layout, container, false);

		return fragmentView;
	}
}
