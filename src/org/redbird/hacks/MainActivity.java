/**
 * The official RedBirdHacks Android application. 
 * 
 * This is the MainActivity that begins by loading a fragment (UpdatesFragment.class) from the activity_main layout.
 * It will then generate the Updates, Mentors, and Schedule tabs at the top of the app and apply a custom tab format.
 * 
 * @author MJ Havens <me@mjhavens.com>, Andrew Erickson
 * Initial creation date: 10/4/2014
 * 
 */

package org.redbird.hacks;

import android.content.Context;
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

		// Initialize and setup the FragmentTabHost
		topTabs = (FragmentTabHost) findViewById(android.R.id.tabhost);
		topTabs.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);

		// Add the three tabs to the top of the screen.
		topTabs.addTab(
				setIndicator(MainActivity.this, topTabs.newTabSpec("Updates"),
						R.drawable.tab_custom_selector, "Updates",
						R.drawable.rss_feed), UpdatesFragment.class, null);
		topTabs.addTab(
				setIndicator(MainActivity.this, topTabs.newTabSpec("Mentors"),
						R.drawable.tab_custom_selector, "Mentors",
						R.drawable.person), MentorsFragment.class, null);
		topTabs.addTab(
				setIndicator(MainActivity.this, topTabs.newTabSpec("Schedule"),
						R.drawable.tab_custom_selector, "Schedule",
						R.drawable.calendar), ScheduleFragment.class, null);

		// Use this to add a custom tab when the tab is pressed, not pressed,
		// etc.
		// TO DO: Make custom icons.
		topTabs.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_custom_selector);
		topTabs.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_custom_selector);
		topTabs.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_custom_selector);

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
