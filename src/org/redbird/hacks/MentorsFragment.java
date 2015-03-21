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
import java.util.Calendar;
import java.util.Collections;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
			
			ImageView mentorContactImage = holder.imageViewMentorContactMethodIcon;
			final ContactMethod mentorContactMethod = mentor.getContactMethod();
			
			//added a nullcheck 
			if(mentorContactMethod != null){
				if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.TWITTER)){
					mentorContactImage.setImageResource(R.drawable.ic_twitter);
				} else if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.FACEBOOK)){
					mentorContactImage.setImageResource(R.drawable.ic_facebook);
				} else if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.EMAIL)){
					mentorContactImage.setImageResource(R.drawable.ic_email);
				} else if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.LINKED_IN)){
					mentorContactImage.setImageResource(R.drawable.ic_linkedin);
				} else if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.PHONE)){
					mentorContactImage.setImageResource(R.drawable.ic_phone);
				}
								
				holder.imageViewMentorContactMethodIcon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = null;
						if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.PHONE)){
							Uri uri = Uri.parse("tel:" + mentorContactMethod.getContactActionURL());
							intent = new Intent(Intent.ACTION_DIAL, uri);
						} else if(mentorContactMethod.getContactMethodType().equals(ContactMethodType.EMAIL)){
							Uri uri = Uri.parse("mailto:" + mentorContactMethod.getContactActionURL());
							intent = new Intent(Intent.ACTION_VIEW, uri);
						} else {
							Uri uri = Uri.parse(mentorContactMethod.getContactActionURL());
							intent = new Intent(Intent.ACTION_VIEW, uri);
						}
						
						if(intent != null)
							startActivity(intent);
											
					}
				});
			}
//			}
			return view;
		}

	}
	
//	JsonFactory factory = new JsonFactory();
//	try {
//		JsonParser jParser = factory.createJsonParser(new URL(url));
//
//		jParser.nextToken();
//
//		Long from = null, to = null;
//		String title = null, description = null;
//
//		while (jParser.getCurrentToken() != JsonToken.END_OBJECT) {
//			jParser.nextToken();
//
//			String fieldname = jParser.getCurrentName();
//
//			if (jParser.getCurrentToken() == JsonToken.START_ARRAY) {
//				break;
//				// jParser.nextToken();
//			}
//
//			if (TAG_EVENTS.equals(fieldname)) {
//				while (jParser.getCurrentToken() != JsonToken.END_OBJECT) {
//					jParser.nextToken();
//
//					fieldname = jParser.getCurrentName();
//					String tokenValue1 = null;
//
//					if (jParser.getCurrentToken() == JsonToken.START_ARRAY) {
//						// break;
//						jParser.nextToken();
//					}
//
//					if (TAG_FROM.equals(fieldname)) {
//						jParser.nextToken();
//
//						tokenValue1 = jParser.getText();
//						if (null != tokenValue1
//								&& tokenValue1.trim().length() > 0)
//							from = Long.parseLong(tokenValue1);
//						Log.d("X", from + "");
//
//					} else if (TAG_TO.equals(fieldname)) {
//						jParser.nextToken();
//
//						tokenValue1 = jParser.getText();
//						if (null != tokenValue1
//								&& tokenValue1.trim().length() > 0)
//							to = Long.parseLong(tokenValue1);
//						Log.d("X", to + "");
//
//					} else if (TAG_TITLE.equals(fieldname)) {
//						jParser.nextToken();
//
//						tokenValue1 = jParser.getText();
//						if (null != tokenValue1
//								&& tokenValue1.trim().length() > 0)
//							title = tokenValue1;
//						Log.d("X", title);
//
//					} else if (TAG_DESCRIPTION.equals(fieldname)) {
//						jParser.nextToken();
//
//						tokenValue1 = jParser.getText();
//						if (null != tokenValue1
//								&& tokenValue1.trim().length() > 0)
//							description = tokenValue1;
//						Log.d("X", description + "");
//					}
//				}
//			}
//
//			// Convert epoch time to a Date.
//			// Unix epoch time is measured in seconds. Multiply by 1000
//			// for milliseconds
//			cal.setTimeInMillis(from * 1000);
//			String month = cal.getDisplayName(Calendar.MONTH, 2,
//					Locale.US);
//			String dayOfMonth = String.valueOf(cal
//					.get(Calendar.DAY_OF_MONTH));
//
//			// Get the from time format and convert from milliseconds to
//			// a time format.
//			String fromTime = fromTime_date_format
//					.format(cal.getTime());
//
//			// Get the to time and convert from milliseconds to a time
//			// format.
//			cal.setTimeInMillis(to * 1000);
//			String toTime = toTime_date_format.format(cal.getTime());
//
//			// Add the event to the event list.
//			events.add(new ScheduleEvent(title, description, fromTime,
//					toTime, month + " " + dayOfMonth));
//		}
//	} catch (Exception e) {
//		// The connection to the server failed. Throw a flag so that we
//		// can catch it later.
//		connectionFailed = true;
//		e.printStackTrace();
//	}

	/**
	 * TO DO: Use the Jackson library to parse JSON.
	 * @author MJ
	 *
	 */
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
			
//			String name = null, specialty = null, description = null;
//			ContactMethod contactMethod = null;
//			List<ContactMethod> contacts = null;
//			ContactMethodType contactMethodType = null;

			//Attempt 2
//			ObjectMapper mapper = new ObjectMapper(); 
//			
//			//so that it doesn't choke on the "eventDate" variable in ScheduleEvent
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			
//			try {
//				URL json = new URL(url);
//				JsonNode node = mapper.readTree(json);
//				node = node.get(TAG_MENTORS);
//				
//				TypeReference<List<Mentor>> typeRef = new TypeReference<List<Mentor>>(){};
//				List<Mentor> mentorList = mapper.readValue(node.traverse(), typeRef);
//				
//				for(Mentor m : mentorList){
//					name = m.getName();
//					specialty = m.getSpecialty();
//					description = m.getDescription();
//					contacts = m.getContactMethodsList();
////					
////					for(ContactMethod c : contacts){
////						contactMethodType = c.getContactMethodType();
////						String contactActionURL = c.getContactActionURL();
////						if(contactActionURL.length() > 0){
////							contactMethod = new ContactMethod(contactMethodType, contactActionURL);
////						}
////						
////					}
//				}
//				
//				Mentor m = new Mentor(name, specialty, contactMethod,
//						description);
//				mentors.add(m);
//				
//					//contactMethod = m.getContactMethod();
//					
////					if (!b.getString("twitter").equals("")) {
////						contactMethod = new ContactMethod(
////								ContactMethodType.TWITTER,
////								b.getString(TAG_TWITTER));
////						//contactMethodsList.add(contactMethod);
////					}
////					if (!b.getString("facebook").equals("")) {
////						contactMethod = new ContactMethod(
////								ContactMethodType.FACEBOOK,
////								b.getString(TAG_FACEBOOK));
////						//contactMethodsList.add(contactMethod);
////					}
////					if (!b.getString("email").equals("")) {
////						contactMethod = new ContactMethod(
////								ContactMethodType.EMAIL, b.getString(TAG_EMAIL));
////						//contactMethodsList.add(contactMethod);
////					}
////					if (!b.getString("linkedin").equals("")) {
////						contactMethod = new ContactMethod(
////								ContactMethodType.LINKED_IN,
////								b.getString(TAG_LINKEDIN));
////						//contactMethodsList.add(contactMethod);
////					}
////					if (!b.getString("phone").equals("")) {
////						contactMethod = new ContactMethod(
////								ContactMethodType.PHONE, b.getString(TAG_PHONE));
////						//contactMethodsList.add(contactMethod);
////					}
//					
//			} catch (Exception e) {
//				// The connection to the server failed. Throw a flag so that we
//				// can catch it later.
//				connectionFailed = true;
//				e.printStackTrace();
//			}
//			
//			if (connectionFailed){
//				mentors.add(new Mentor("Server Connection Error",
//						"Please check your connection", contacts, ""));
//			}
//
//			return mentors;
			
			
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

					//List<ContactMethod> contactMethodsList = new ArrayList<ContactMethod>();
					JSONArray contactMethodsArray = a.getJSONArray(TAG_CONTACT);

					JSONObject b = contactMethodsArray.getJSONObject(0);
					
					ContactMethod contactMethod = null;

					if (!b.getString("twitter").equals("")) {
						contactMethod = new ContactMethod(
								ContactMethodType.TWITTER,
								b.getString(TAG_TWITTER));
						//contactMethodsList.add(contactMethod);
					}
					if (!b.getString("facebook").equals("")) {
						contactMethod = new ContactMethod(
								ContactMethodType.FACEBOOK,
								b.getString(TAG_FACEBOOK));
						//contactMethodsList.add(contactMethod);
					}
					if (!b.getString("email").equals("")) {
						contactMethod = new ContactMethod(
								ContactMethodType.EMAIL, b.getString(TAG_EMAIL));
						//contactMethodsList.add(contactMethod);
					}
					if (!b.getString("linkedin").equals("")) {
						contactMethod = new ContactMethod(
								ContactMethodType.LINKED_IN,
								b.getString(TAG_LINKEDIN));
						//contactMethodsList.add(contactMethod);
					}
					if (!b.getString("phone").equals("")) {
						contactMethod = new ContactMethod(
								ContactMethodType.PHONE, b.getString(TAG_PHONE));
						//contactMethodsList.add(contactMethod);
					}
					Mentor m = new Mentor(name, specialty, contactMethod,
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
			
			if (connectionFailed){
				mentors.add(new Mentor("Server Connection Error",
						"Please check your connection", null, ""));
			}
			
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
