/**
*  Updates.java
*  RedbirdHacks
*  
* This POJO class stores the attributes for a single update.
*
* Created by MJ Havens on 10/18/14.
**/
package org.redbird.hacks;


public class Updates
{
	private String	date;
	private String	text;

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
