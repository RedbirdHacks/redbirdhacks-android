package org.redbird.hacks;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class is used to set the Updates attributes to an item in the ListView
 * 
 * @author - MJ Havens <me@mjhavens.com> on 10/18/2014
 */
public class UpdatesListAdapter extends ArrayAdapter<Updates>
{
	private int		resource;
	private Context	ctx;

	/**
	 * Constructor for the class.
	 * 
	 * @param ctx
	 *            - The context. It will be the MainActivity.
	 * @param resourceId
	 *            - The layout resource that we will be inflating -
	 *            updates_listview.xml
	 */
	public UpdatesListAdapter(Context ctx, int resourceId, List<Updates> objects)
	{
		super(ctx, resourceId, objects);
		this.ctx = ctx;
		resource = resourceId;
	}

	/**
	 * Sets the update text and update description for an update. 
	 * 
	 * Iterates through each update and changes the format for the words that have hashtags.
	 * This will make them stand out easier for the user, and look more appealing.
	 * Returns a single view for each item in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(resource,
					parent, false);

		Updates update = getItem(position);
		String updateText = update.getText();
		StringBuilder formattedHashtagText = new StringBuilder(updateText);
		int paddingSize = 0; // The character count for the number of html tags
								// that have been added
		final int htmlTagSize = 22; // The character count of a single html tag.
		final int totalHtmlTagSize = 29; // The character
															// count of both the
															// starting and
															// ending html tags
															// plus the /

		for (int i = 0; i < updateText.length(); i++)
		{
			// If there is a character that starts with a hashtag...
			if (updateText.charAt(i) == '#')
			{
				// We want to make the hashtag word bold
				// Add the html bold tag before the start of the hashtag.
				formattedHashtagText.insert(i + paddingSize, "<font color=\"#E81A08\">");

				// Make a substring from the start of the hastag to the end of
				// the sentence.
				String hashtagAndOn = updateText.substring(i,
						updateText.length());

				// The end of the hashtag word is either a space, or null.
				// Get the index of the space.
				int hashtagEnding = hashtagAndOn.indexOf(" ");
				// If there was no space, this means that the hashtag word was
				// at the very end of the sentence.
				if (hashtagEnding == -1)
				{
					// The end of the hashtag word is the length of the
					// substring made earlier, the index in the loop, the
					// paddingSize, and the htmlTagSize (because we just added
					// another starting html tag.
					hashtagEnding = hashtagAndOn.length() + i + paddingSize
							+ htmlTagSize;
				}
				else
				{
					// The end of the hashtag is the sum of the loop index, the
					// paddingSize, and the htmlTagSize (because we just added
					// another starting html tag.
					hashtagEnding += i + paddingSize + htmlTagSize;
				}
				formattedHashtagText.insert(hashtagEnding, "</font>");
				Log.d("APP", formattedHashtagText.toString());

				// Add onto the padding size in case there is another hashtag in the sentence.
				paddingSize += totalHtmlTagSize;
			}
		}
		TextView tvUpdateText = (TextView) convertView
				.findViewById(R.id.updateTitle);
		tvUpdateText.setText(Html.fromHtml(formattedHashtagText.toString()));


		TextView tvUpdateDate = (TextView) convertView
				.findViewById(R.id.updateDesc);
		tvUpdateDate.setText(update.getDate());

		// Use this if we want to set an image next to each list item.

		// ImageView updateImage = (ImageView)
		// convertView.findViewById(R.id.updateImage);
		// String uri = "drawable/" + updates.getImage();
		// int imageResource = context.getResources().getIdentifier(uri, null,
		// context.getPackageName());
		// Drawable image = context.getResources().getDrawable(imageResource);
		// updateImage.setImageDrawable(image);
		((ArrayAdapter<?>) UpdatesListAdapter.this).notifyDataSetChanged();

		return convertView;
	}
}
