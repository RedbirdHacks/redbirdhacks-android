/**
*  ScheduleEvent.java
*  RedbirdHacks
*
*  Created by Andrew Erickson on 11/1/14.
**/
package org.redbird.hacks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduleEvent implements Comparable<ScheduleEvent>{
	
	private String eventTitle;
	private String eventDescription;
	private String fromTime;
	private String toTime;
	private String eventDate;
	
	@JsonCreator
	public ScheduleEvent(@JsonProperty("title") String eventTitle, @JsonProperty("description") String eventDescription,
			@JsonProperty("from") String fromTime, @JsonProperty("to") String toTime) {
		super();
		this.eventTitle = eventTitle;
		this.eventDescription = eventDescription;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	
	public ScheduleEvent(String eventTitle, String eventDescription,
			String fromTime, String toTime, String eventDate) {
		super();
		this.eventTitle = eventTitle;
		this.eventDescription = eventDescription;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.eventDate = eventDate;
	}
	
	/**
	 * @return the fromTime
	 */
	public String getFromTime()
	{
		return fromTime;
	}

	/**
	 * @param fromTime the fromTime to set
	 */
	public void setFromTime(String fromTime)
	{
		this.fromTime = fromTime;
	}

	/**
	 * @return the eventTitle
	 */
	public String getEventTitle()
	{
		return eventTitle;
	}

	/**
	 * @param eventTitle the eventTitle to set
	 */
	public void setEventTitle(String eventTitle)
	{
		this.eventTitle = eventTitle;
	}

	/**
	 * @return the eventDescription
	 */
	public String getEventDescription()
	{
		return eventDescription;
	}

	/**
	 * @param eventDescription the eventDescription to set
	 */
	public void setEventDescription(String eventDescription)
	{
		this.eventDescription = eventDescription;
	}

	/**
	 * @return the toTime
	 */
	public String getToTime()
	{
		return toTime;
	}

	/**
	 * @param toTime the toTime to set
	 */
	public void setToTime(String toTime)
	{
		this.toTime = toTime;
	}

	/**
	 * @return the eventDate
	 */
	public String getEventDate()
	{
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(String eventDate)
	{
		this.eventDate = eventDate;
	}

	@Override
	public int compareTo(ScheduleEvent another) {
		if(this.eventDate.compareTo(another.getEventDate()) == 0){
			return this.fromTime.compareTo(another.getFromTime()) > 0 ? 1 : 0;
		}else if (this.eventDate.compareTo(another.getEventDate()) > 0){
			return 1;
		}else {
			return -1;
		}
	}
}