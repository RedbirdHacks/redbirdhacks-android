/**
 * The official RedbirdHacks Android application. 
 * 
 * This is the MainActivity that begins by loading custom tabs at the top,
 * and then the Updates fragment is loaded.
 * 
 * @author MJ Havens <me@mjhavens.com> 
 * @author Andrew Erickson
 * 
 * Initial creation date: 10/4/2014
 * 
 */

package org.redbird.hacks;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends FragmentActivity
{
	private FragmentTabHost	topTabs;

	/**
	 * Adds the tabs to the very top of the app and sets the updatesFragment as
	 * the initial screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("MainActivity", "onCreate");

		
		//TODO: Notifications for new updates
		startService(new Intent(this, NotificationService.class));
				
		// Initialize and setup the FragmentTabHost
		topTabs = (FragmentTabHost) findViewById(android.R.id.tabhost);
		topTabs.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);

		// Add the three tabs to the top of the screen.
		topTabs.addTab(
				setIndicator(MainActivity.this, topTabs.newTabSpec("Updates"),
						R.drawable.tab_background_selector, "Updates",
						R.drawable.updates_tab_icon_selector), UpdatesFragment.class, null);
		topTabs.addTab(
				setIndicator(MainActivity.this, topTabs.newTabSpec("Mentors"),
						R.drawable.tab_background_selector, "Mentors",
						R.drawable.mentors_tab_icon_selector), MentorsFragment.class, null);
		topTabs.addTab(
				setIndicator(MainActivity.this, topTabs.newTabSpec("Schedule"),
						R.drawable.tab_background_selector, "Schedule",
						R.drawable.schedule_tab_icon_selector), ScheduleFragment.class, null);

		// Use this to add a custom tab when the tab is pressed, not pressed,
		// etc.
		// TO DO: Make custom icons.
		topTabs.getTabWidget().setBackgroundResource(R.drawable.tab_background_selector);
		topTabs.getTabWidget().setDividerDrawable(null);
		
		// This gets called whenever a user clicks on a notification for a new update. 
		// The notification will bring them to a specific fragment depending on the type of
		// notification. After they are brought to the fragment, the notification is cleared.
		int tabToOpen = getIntent().getIntExtra("fragmentNum", 0);
		if(tabToOpen != -1) { 
			topTabs.setCurrentTab(tabToOpen);
			int notificationID = getIntent().getIntExtra("notificationID", 0);
			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel("com.redbird.hacks.notification", notificationID);
		} 

	}

	private TabSpec setIndicator(Context ctx, TabSpec spec, 	int resid,
			String string, int genresIcon)
	{
		View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
		v.setBackgroundResource(resid);
		TextView tv = (TextView) v.findViewById(R.id.txt_tabtxt);
		ImageView img = (ImageView) v.findViewById(R.id.img_tabtxt);

		tv.setText(string);
		img.setBackgroundResource(genresIcon);
		return spec.setIndicator(v);
	}
}
