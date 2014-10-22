/**
 * The official RedBirdHacks Android application. 
 * 
 * This is the MainActivity that begins by loading a fragment (UpdatesFragment.class) from the activity_main layout.
 * It will then generate the Updates, Mentors, and Schedule tabs at the top of the app and apply a custom tab format.
 * 
 * @author MJ Havens <me@mjhavens.com>
 */

package org.redbird.hacks;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

public class MainActivity extends FragmentActivity
{
	private FragmentTabHost	topTabs;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
  
		super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			Log.i("MainActivity", "onCreate");

			// Initialize and setup the FragmentTabHost
			topTabs = (FragmentTabHost) findViewById(android.R.id.tabhost);
			topTabs.setup(this, getSupportFragmentManager(),
					android.R.id.tabcontent);

			// Add the three tabs to the top of the screen.
			topTabs.addTab(topTabs.newTabSpec("Updates")
					.setIndicator("Updates"), UpdatesFragment.class, null);
			topTabs.addTab(topTabs.newTabSpec("Mentors")
					.setIndicator("Mentors"), UpdatesFragment.class, null);
			topTabs.addTab(
					topTabs.newTabSpec("Schedule").setIndicator("Schedule"),
					ScheduleFragment.class, null);

		// Use this to add a custom tab when the tab is pressed, not pressed,
		// etc.
		// TO DO: Make custom icons.
		// topTabs.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_custom_selector);
		// topTabs.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_custom_selector);
		// topTabs.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_custom_selector);

	}
}
