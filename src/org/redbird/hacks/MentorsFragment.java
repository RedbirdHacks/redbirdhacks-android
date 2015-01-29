/**
 *  MentorsFragment.java
 *  RedbirdHacks
 *
 *  Created by Andrew Erickson and MJ Havens in 2014.
 **/
package org.redbird.hacks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redbird.hacks.SimpleSectionAdapter.Sectionizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MentorsFragment extends ListFragment {

	private View fragmentView;

	private List<Mentor> mentorList = new ArrayList<Mentor>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.mentors_fragment_layout,
				container, false);

		return fragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mentorList.isEmpty()) {
			try {
				mentorList.addAll(new RetrieveMentors().execute().get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Collections.sort(mentorList);

		MentorAdapter mentorAdapter = new MentorAdapter(getActivity(),
				R.layout.methods_list_item, mentorList);

		MentorSectionizer mentorSectionizer = new MentorSectionizer();

		SimpleSectionAdapter<Mentor> sectionAdapter = new SimpleSectionAdapter<Mentor>(
				getActivity(), mentorAdapter, R.layout.mentor_section_header,
				R.id.tvMentorSectionHeaderTitle, mentorSectionizer);

		setListAdapter(sectionAdapter);

	}

	private class MentorAdapter extends ArrayAdapter<Mentor> {

		private List<Mentor> mentors;

		public MentorAdapter(Context context, int textViewResourceId,
				List<Mentor> mentors) {
			super(context, textViewResourceId, mentors);
			this.mentors = mentors;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			MentorHolder holder = null;

			if (view == null) {
				view = View.inflate(getActivity(), R.layout.methods_list_item,
						null);

				holder = new MentorHolder();
				holder.tvMentorName = (TextView) view
						.findViewById(R.id.tvMentorName);
				holder.imageViewMentorContactMethodIcon = (ImageView) view
						.findViewById(R.id.imageViewMentorContactMethodIcon);
				view.setTag(holder);
			} else {
				holder = (MentorHolder) view.getTag();
			}

			Mentor mentor = mentors.get(position);
			holder.tvMentorName.setText(mentor.getName());
			// TODO -- What's the preferred contact method?
			List<ContactMethod> x = (List<ContactMethod>) mentor.getContactMethodsList();
			
			//Twitter: ic_launcher
			//Facebook: genres_icon
			//Email: rss_feed
			//LinkedIn: tab_active
			//Phone: ic_launcher
			if(x != null){
				//since we're not sure how to get a preferred
				//contact, just getting the first one off the list
				final ContactMethod m = x.get(0);
				if(m.getContactMethodType().equals(ContactMethodType.TWITTER)){
					holder.imageViewMentorContactMethodIcon.setBackgroundResource(R.drawable.ic_launcher);
				} else if(m.getContactMethodType().equals(ContactMethodType.FACEBOOK)){
					holder.imageViewMentorContactMethodIcon.setBackgroundResource(R.drawable.genres_icon);
				} else if(m.getContactMethodType().equals(ContactMethodType.EMAIL)){
					holder.imageViewMentorContactMethodIcon.setBackgroundResource(R.drawable.rss_feed);
				} else if(m.getContactMethodType().equals(ContactMethodType.LINKED_IN)){
					holder.imageViewMentorContactMethodIcon.setBackgroundResource(R.drawable.tab_active);
				} else if(m.getContactMethodType().equals(ContactMethodType.PHONE)){
					holder.imageViewMentorContactMethodIcon.setBackgroundResource(R.drawable.ic_launcher);
				}
				
				holder.imageViewMentorContactMethodIcon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Uri uri = Uri.parse(m.getContactActionURL());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);						
					}
				});
			}
			return view;
		}

	}

	private class RetrieveMentors extends AsyncTask<Void, Void, List<Mentor>> {
		private final String TAG_MENTORS = "mentors";
		private final String TAG_NAME = "name";
		private final String TAG_SPECIALTY = "specialty";
		private final String TAG_CONTACT = "contacts";
		private final String TAG_TWITTER = "twitter";
		private final String TAG_FACEBOOK = "facebook";
		private final String TAG_EMAIL = "email";
		private final String TAG_LINKEDIN = "linkedin";
		private final String TAG_PHONE = "phone";
		private final String TAG_DESCRIPTION = "description";
		private boolean connectionFailed;

		@Override
		protected List<Mentor> doInBackground(Void... params) {
			List<Mentor> mentors = new ArrayList<Mentor>();
			final String url = "http://redbirdhacks.org/json/mentors.json";
			connectionFailed = false;
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httppost = new HttpPost(url);

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
				JSONArray mentorsArray = jObject.getJSONArray(TAG_MENTORS);

				// Grab the data for each mentor.
				for (int i = 0; i < mentorsArray.length(); i++) {
					JSONObject a = mentorsArray.getJSONObject(i);
					String name = a.getString(TAG_NAME);
					String specialty = a.getString(TAG_SPECIALTY);
					String description = a.getString(TAG_DESCRIPTION);

					List<ContactMethod> contactMethodsList = new ArrayList<ContactMethod>();
					JSONArray contactMethodsArray = a.getJSONArray(TAG_CONTACT);

					JSONObject b = contactMethodsArray.getJSONObject(0);

					if (!b.getString("twitter").equals("")) {
						ContactMethod c = new ContactMethod(
								ContactMethodType.TWITTER,
								b.getString(TAG_TWITTER));
						contactMethodsList.add(c);
					}
					if (!b.getString("facebook").equals("")) {
						ContactMethod c = new ContactMethod(
								ContactMethodType.FACEBOOK,
								b.getString(TAG_FACEBOOK));
						contactMethodsList.add(c);
					}
					if (!b.getString("email").equals("")) {
						ContactMethod c = new ContactMethod(
								ContactMethodType.EMAIL, b.getString(TAG_EMAIL));
						contactMethodsList.add(c);
					}
					if (!b.getString("linkedin").equals("")) {
						ContactMethod c = new ContactMethod(
								ContactMethodType.LINKED_IN,
								b.getString(TAG_LINKEDIN));
						contactMethodsList.add(c);
					}
					if (!b.getString("phone").equals("")) {
						ContactMethod c = new ContactMethod(
								ContactMethodType.PHONE, b.getString(TAG_PHONE));
						contactMethodsList.add(c);
					}
					Mentor m = new Mentor(name, specialty, contactMethodsList,
							description);
					mentors.add(m);
				}
			} catch (Exception e) {
				// The connection to the server failed. Throw a flag so that we
				// can catch it later.
				connectionFailed = true;
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (Exception squish) {
				}
			}
			if (connectionFailed)
				mentors.add(new Mentor("Server Connection Error",
						"Please check your connection", null, ""));

			return mentors;

		}
	}

	static class MentorHolder {
		public TextView tvMentorName;
		public ImageView imageViewMentorContactMethodIcon;
	}

	private class MentorSectionizer implements Sectionizer<Mentor> {

		public MentorSectionizer() {
		}

		@Override
		public String getSectionTitleForItem(Mentor instance) {
			return instance.getSpecialty();
		}
	}
}
