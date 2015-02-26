/**
 *  NotificationService.java
 *  RedbirdHacks
 *
 *  @author - MJ Havens <me@mjhavens.com>
 *  Created on 2/22/2015
 *  
 *  This service will periodically check the JSON for any new updates that have come. 
 *  It does this by comparing the last known update date to the newly retrieved list of updates.
 *  If a new update is found, it will give the user a notification.
 **/

package org.redbird.hacks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service
{

	private String						TAG				= "APP";
	private final String				updatesURL		= "http://mjhavens.com/announcements.json";
	private final String				eventsURL		= "http://mjhavens.com/events.json";
	private Handler						handler			= new Handler();
	private Runnable					runnable;
	private long						oldUpdateTime	= -1;
	private ScheduledExecutorService	updatesTaskExecutor;
	private ScheduledExecutorService	eventsTaskExecutor;
	ScheduledFuture<?>					updatesResult;
	ScheduledFuture<?>					eventsResult;
	BroadcastReceiver					mReceiver;
	NotificationManager					notificationManager;
	int									notificationID	= 0;

	/*
	 * This class is used to capture broadcasts sent by the UpdatesFragment
	 * class. The broadcast tells us that the user has refreshed the updates
	 * screen. We will store the latest update time in this class so we know
	 * that the user has already seen that update and a notification does not
	 * need to be displayed.
	 */
	public class MyReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle extras = intent.getExtras();
			if (extras != null)
			{
				if (extras.containsKey("newestTime"))
				{
					if (extras.getLong("newestTime") != -1)
					{
						oldUpdateTime = extras.getLong("newestTime");
						Log.d(TAG, "Received the broadcast: " + oldUpdateTime);
					}
				}
			}
		}

		public MyReceiver()
		{

		}
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	/**
	 * Called upon the creation of this class in MainActivity. This registers
	 * the BroadcastReceiver and starts a schedule to look for new updates every
	 * minute.
	 */
	@Override
	public void onCreate()
	{
		Log.d(TAG, "onCreate");

		IntentFilter filter = new IntentFilter("com.redbird.hacks.MESSAGE");
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Schedule to check for new updates every minute.
		updatesTaskExecutor = Executors.newSingleThreadScheduledExecutor();
		updatesResult = updatesTaskExecutor.scheduleAtFixedRate(new Runnable()
		{
			public void run()
			{
				Log.d(TAG, "schedule task executor");
				new JSONUpdates().execute(updatesURL);
			}
		}, 0, 2, TimeUnit.MINUTES);

		// Schedule to check for new updates every minute.
		eventsTaskExecutor = Executors.newSingleThreadScheduledExecutor();
		eventsResult = eventsTaskExecutor.scheduleAtFixedRate(new Runnable()
		{
			public void run()
			{
				Log.d(TAG, "schedule task executor");
				new JSONEvents().execute(eventsURL);
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	@Override
	public void onDestroy()
	{
		unregisterReceiver(mReceiver);
		handler.removeCallbacks(runnable);
		try
		{
			updatesResult.cancel(false);
			eventsResult.cancel(false);

		}
		catch (Exception e)
		{

		}
		Log.d(TAG, "onDestroy");
		stopSelf();
	}

	/**
	 * TO DO: Use the Jackson library to parse JSON.
	 * 
	 * @author MJ
	 *
	 */
	private class JSONUpdates extends AsyncTask<String, Void, Void>
	{
		private final String	TAG_UPDATES			= "announcements";
		private final String	TAG_UPDATES_DATE	= "date";
		private final String	TAG_UPDATES_TEXT	= "text";
		private Calendar		cal					= Calendar.getInstance();

		@Override
		protected Void doInBackground(String... url)
		{
			Log.d(TAG, "doInBackground updates");
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httppost = new HttpPost(url[0]);

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

				// Grab the first update in the array. This will be the newest.
				JSONArray updates = jObject.getJSONArray(TAG_UPDATES);
				if (updates != null && updates.length() > 0)
				{
					JSONObject a = updates.getJSONObject(0);
					long newestUpdateTime = a.getLong(TAG_UPDATES_DATE);
					if (oldUpdateTime != -1
							&& (newestUpdateTime > oldUpdateTime))
					{
						Log.d(TAG, "WE HAVE A NEW UPDATE!");
						Notification.Builder noti = new Notification.Builder(
								getApplicationContext())
								.setContentTitle("New Update!")
								.setContentText(a.getString(TAG_UPDATES_TEXT))
								.setSmallIcon(R.drawable.ic_launcher);
						oldUpdateTime = newestUpdateTime;
						Intent notificationIntent = new Intent(
								getApplicationContext(), MainActivity.class);
						notificationIntent.putExtra("fragmentNum", 0);
						notificationIntent.putExtra("notificationID",
								notificationID);

						PendingIntent resultPendingIntent = PendingIntent
								.getActivity(getApplicationContext(), 0,
										notificationIntent,
										PendingIntent.FLAG_UPDATE_CURRENT);
						noti.setContentIntent(resultPendingIntent);

						notificationManager.notify(
								"com.redbird.hacks.notification",
								notificationID, noti.build());
						notificationID++;
						notificationID = (notificationID >= Integer.MAX_VALUE) ? 0
								: notificationID;
						Log.d("APP", "Updates time: " + newestUpdateTime);
					}
					else if (oldUpdateTime == -1)
					{
						// In case the activity is destroyed and re-created, we
						// want to make sure that we have a value
						// set for the oldUpdateTime. Otherwise, the user would
						// never receive a notification again
						// unless they refreshed the updates screen.
						oldUpdateTime = newestUpdateTime;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// The connection to the server failed.
				// We don't want to display any error messages since this is
				// just a routine check for new updates.
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
			return null;
		}
	}

	/**
	 * TO DO: Use the Jackson library to parse JSON.
	 * 
	 * @author MJ
	 *
	 */
	private class JSONEvents extends AsyncTask<String, Void, Void>
	{
		private final String		TAG_EVENTS				= "events";
		private final String		TAG_FROM				= "from";
		private final String		TAG_TO					= "to";
		private final String		TAG_TIME				= "time";
		private final String		TAG_TITLE				= "title";
		private final String		TAG_DESCRIPTION			= "description";
		private boolean				connectionFailed;
		private SimpleDateFormat	fromTime_date_format	= new SimpleDateFormat(
																	"h:mm");
		private SimpleDateFormat	toTime_date_format		= new SimpleDateFormat(
																	"h:mm");
		private Calendar			cal						= Calendar
																	.getInstance();

		@Override
		protected Void doInBackground(String... url)
		{
			Log.d("APP", "Getting Events");
			List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
			connectionFailed = false;
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httppost = new HttpPost(url[0]);

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

				// Grab each event.
				for (int i = 0; i < eventsArray.length(); i++)
				{
					JSONObject a = eventsArray.getJSONObject(i);
					Long from = a.getLong(TAG_FROM);
					String eventTitle = a.getString(TAG_TITLE);

					// Convert epoch time to a Date.
					// Unix epoch time is measured in seconds. Multiply by 1000
					// for milliseconds
					cal.setTimeInMillis(from * 1000);
					long diffInMillis = (cal.getTimeInMillis() - System
							.currentTimeMillis());
					System.out.println("Difference in millis: " + diffInMillis);
					long diffInMinutes = TimeUnit.MILLISECONDS
							.toMinutes(diffInMillis);
					System.out.println("Diff minutes: " + diffInMinutes);

					if (diffInMinutes > 0 && diffInMinutes <= 15)
					{
						Log.d(TAG, "An event is happening soon");
						String title = diffInMinutes > 1 ? ("Event in "
								+ diffInMinutes + " minutes") : ("Event in "
								+ diffInMinutes + " minute");
						Notification.Builder noti = new Notification.Builder(
								getApplicationContext()).setContentTitle(title)
								.setContentText(eventTitle)
								.setSmallIcon(R.drawable.ic_launcher);
						Intent notificationIntent = new Intent(
								getApplicationContext(), MainActivity.class);
						notificationIntent.putExtra("fragmentNum", 0);
						notificationIntent.putExtra("notificationID",
								notificationID);
						
						PendingIntent resultPendingIntent = PendingIntent
								.getActivity(getApplicationContext(), 0,
										notificationIntent,
										PendingIntent.FLAG_UPDATE_CURRENT);
						noti.setContentIntent(resultPendingIntent);

						notificationManager.notify(
								"com.redbird.hacks.notification",
								notificationID, noti.build());
						notificationID++;
						notificationID = (notificationID >= Integer.MAX_VALUE) ? 0
								: notificationID;
					}

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// The connection to the server failed.
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

			return null;

		}

	}

}
