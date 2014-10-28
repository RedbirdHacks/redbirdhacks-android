/**
 * This class is used to set the Updates attributes to an item in the ListView.
 * 
 * @author MJ Havens <me@mjhavens.com>
 */

package org.redbird.hacks;

/**
 * The adapter class that was made to return each list item as a view.
 * 
 * @author - MJ Havens <me@mjhavens.com>
 */

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UpdatesListAdapter extends ArrayAdapter<Updates>
{
	private int				resource;
	private LayoutInflater	inflater;
	private Context			context;

	/**
	 * Constructor for the class.
	 * 
	 * @param ctx - The context. It will be the MainActivity.
	 * @param resourceId - The layout resource that we will be inflating - updates_listview.xml
	 */
	public UpdatesListAdapter(Context ctx, int resourceId, List<Updates> objects)
	{
		super(ctx, resourceId, objects);
		resource = resourceId; //
		inflater = LayoutInflater.from(ctx);
		context = ctx;
	}

	/**
	 * Sets the update text and update description for an update. 
	 * It then returns a single view for each item in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(resource,
					parent, false);

		Updates update = getItem(position);
		TextView updateText = (TextView) convertView
				.findViewById(R.id.updateTitle);
		updateText.setText(update.getText());

		TextView updateDate = (TextView) convertView
				.findViewById(R.id.updateDesc);
		updateDate.setText(update.getDate());

		// Use this if we want to set an image next to each list item.

		// ImageView updateImage = (ImageView)
		// convertView.findViewById(R.id.updateImage);
		// String uri = "drawable/" + updates.getImage();
		// int imageResource = context.getResources().getIdentifier(uri, null,
		// context.getPackageName());
		// Drawable image = context.getResources().getDrawable(imageResource);
		// updateImage.setImageDrawable(image);
		((ArrayAdapter) UpdatesListAdapter.this).notifyDataSetChanged();

		return convertView;
	}
}
