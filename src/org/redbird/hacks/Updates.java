
/**
 * This POJO class stores the attributes for a single announcement.
 * 
 * @author MJ Havens
 *
 */
package org.redbird.hacks;

public class Updates
{
	private String	description;
	private String	title;

	/**
	 * Basic constructor.
	 * 
	 * @param title - The title of the announcement item.
	 * @param description - the description of the announcement item.
	 */
	public Updates(String title, String description)
	{
		super();
		this.title = title;
		this.description = description;
	}
	
	/**
	 * Gets the title of an announcement.
	 * 
	 * @return the title of an announcement.
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Sets the title of an announcement.
	 * 
	 * @param title the title of an announcement.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Gets the description of the announcement.
	 * 
	 * @return the description of the announcement.
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Sets the description of the announcement.
	 * 
	 * @return the description of the announcement.
	 */	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
