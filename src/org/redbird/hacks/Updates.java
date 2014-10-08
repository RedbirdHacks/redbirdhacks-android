/**
 * 
 */
package org.redbird.hacks;

/**
 * Object for the Updates
 * 
 * @author MJ Havens
 *
 */
public class Updates
{
	private String	description;
	private String	title;

	public Updates(String title, String description)
	{
		super();
		this.title = title;
		this.description = description;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
