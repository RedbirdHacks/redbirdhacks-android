package org.redbird.hacks;

import org.redbird.hacks.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainActivity extends FragmentActivity
{
	// Fragment TabHost as topTabs

    private FragmentTabHost	topTabs;
    

	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        topTabs = (FragmentTabHost)findViewById(android.R.id.tabhost);
        topTabs.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        topTabs.addTab(topTabs.newTabSpec("simple").setIndicator("Updates"),
                UpdatesFragment.class, null);
        topTabs.addTab(topTabs.newTabSpec("contacts").setIndicator("Mentors"),
        		UpdatesFragment.class, null);
        topTabs.addTab(topTabs.newTabSpec("custom").setIndicator("Schedule"),
        		UpdatesFragment.class, null);
    }
}
