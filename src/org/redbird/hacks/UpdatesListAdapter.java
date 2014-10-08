/**
 * This class is used to set the Updates attributes to a list item.
 * 
 * @author MJ Havens
 */

package org.redbird.hacks;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(resource,
					parent, false);

		Updates update = getItem(position);
		TextView updateTitle = (TextView) convertView
				.findViewById(R.id.updateTitle);
		updateTitle.setText(update.getTitle());

		TextView updateDesc = (TextView) convertView
				.findViewById(R.id.updateDesc);
		updateDesc.setText(update.getDescription());

		// Use this if we want to set an image next to each list item.

		// ImageView updateImage = (ImageView)
		// convertView.findViewById(R.id.updateImage);
		// String uri = "drawable/" + updates.getImage();
		// int imageResource = context.getResources().getIdentifier(uri, null,
		// context.getPackageName());
		// Drawable image = context.getResources().getDrawable(imageResource);
		// updateImage.setImageDrawable(image);

		return convertView;
	}
}
