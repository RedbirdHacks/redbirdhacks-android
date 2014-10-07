package org.redbird.hacks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class UpdatesFragment extends Fragment
{
	private ListView	listViewUpdates;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
        // Inflate the layout for this fragment
    	View rootView = inflater.inflate(R.layout.updates_fragment_layout, container, false);
    	
        List<Updates> legendList= new ArrayList<Updates>();
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));
        legendList.add(new Updates("Breakfast has arrived!","We will be serving eggs and sausage"));

        listViewUpdates = ( ListView ) rootView.findViewById( R.id.updatesFeed_List);
        listViewUpdates.setAdapter( new UpdatesListAdapter(getActivity(), R.layout.updates_listview, legendList ) );
   
        listViewUpdates.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Updates o = (Updates) parent.getItemAtPosition(position); 
                Toast.makeText(getActivity(), o.getTitle().toString(), Toast.LENGTH_SHORT).show();
                }
        });            
        return rootView;
    }
}
