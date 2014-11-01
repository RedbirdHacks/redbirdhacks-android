package org.redbird.hacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.redbird.hacks.SimpleSectionAdapter.Sectionizer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MentorsFragment extends ListFragment {
	
	private View fragmentView;
	
	private List<Mentor> mentorList = new ArrayList<Mentor>();
	
//	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.mentors_fragment_layout, container, false);

		return fragmentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(mentorList.isEmpty()){
			mentorList.addAll(stubMentorsList());
		}
		
		Collections.sort(mentorList);
		
		MentorAdapter mentorAdapter = new MentorAdapter(getActivity(), R.layout.methods_list_item, mentorList);
		
		MentorSectionizer mentorSectionizer = new MentorSectionizer();
		
		SimpleSectionAdapter<Mentor> sectionAdapter = new SimpleSectionAdapter<Mentor>(getActivity(), mentorAdapter, R.layout.mentor_section_header, R.id.tvMentorSectionHeaderTitle, mentorSectionizer);
		
		setListAdapter(sectionAdapter);
		
	}

	private List<Mentor> stubMentorsList() {
		List<Mentor> mentors = new ArrayList<Mentor>();
		
		mentors.add(new Mentor("Tallyn Turnbow", "iOS Development", new ContactMethod(ContactMethodType.TWITTER, "t.co/turnbow")));
		mentors.add(new Mentor("Andrew Erickson", "Android Development", new ContactMethod(ContactMethodType.SMS, "3095551234")));
		mentors.add(new Mentor("MJ Havens", "Android Development", new ContactMethod(ContactMethodType.SMS, "3095551234")));
		return mentors;
	}
	
	private class MentorAdapter extends ArrayAdapter<Mentor>{
		
		private List<Mentor> mentors;
		
		public MentorAdapter(Context context, int textViewResourceId, List<Mentor> mentors){
			super(context, textViewResourceId, mentors);
			this.mentors = mentors;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
            MentorHolder holder = null;

            if(view == null) {
                view = View.inflate(getActivity(), R.layout.methods_list_item, null);

                holder = new MentorHolder();
                holder.tvMentorName = (TextView) view.findViewById(R.id.tvMentorName);
//                holder.geoPointTextView = (TextView) view.findViewById(R.id.geo_point);

                view.setTag(holder);
            } else {
                holder = (MentorHolder) view.getTag();
            }
            
            Mentor mentor = mentors.get(position);
            holder.tvMentorName.setText(mentor.getName());
            //TODO -- Load contact icon based on preferred contact method
			
			return view;
		}
		
		
	}
	static class MentorHolder{
		public TextView tvMentorName;
		public ImageView imageViewMentorContactMethodIcon;
	}
	
	class MentorSectionizer implements Sectionizer<Mentor>{
//		private Mentor currentMentor;
		
		public MentorSectionizer(){
//			this.currentMentor = currentMentor;
		}

		@Override
		public String getSectionTitleForItem(Mentor instance) {
			return instance.getExpertise();
		}
	}
}
