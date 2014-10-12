
/**
 * This POJO class stores the attributes for a single announcement/update.
 * 
 * @author MJ Havens
 *
 */
package org.redbird.hacks;

public class Updates
{
	private String	date;
	private String	text;

	/**
	 * Basic constructor.
	 * 
	 * @param title - The title of the announcement item.
	 * @param description - the description of the announcement item.
	 */
	public Updates(String text, String date)
	{
		super();
		this.text = text;
		this.date = date;
	}
	
	/**
	 * Gets the title of an announcement.
	 * 
	 * @return the title of an announcement.
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Sets the title of an announcement.
	 * 
	 * @param title the title of an announcement.
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 * Gets the description of the announcement.
	 * 
	 * @return the description of the announcement.
	 */
	public String getDate()
	{
		return date;
	}
	
	/**
	 * Sets the description of the announcement.
	 * 
	 * @return the description of the announcement.
	 */	
	public void setDate(String date)
	{
		this.date = date;
	}
}
