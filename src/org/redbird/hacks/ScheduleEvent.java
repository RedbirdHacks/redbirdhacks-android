package org.redbird.hacks;

public class ScheduleEvent implements Comparable<ScheduleEvent>{
	
	private String eventTitle;
	private String eventDescription;
	private String eventTime;
	private String eventDate;
	
	public ScheduleEvent(String eventTitle, String eventDescription,
			String eventTime, String eventDate) {
		super();
		this.eventTitle = eventTitle;
		this.eventDescription = eventDescription;
		this.eventTime = eventTime;
		this.eventDate = eventDate;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	@Override
	public int compareTo(ScheduleEvent another) {
		if(this.eventDate.compareTo(another.getEventDate()) == 0){
			return this.eventTime.compareTo(another.getEventTime()) > 0 ? 1 : 0;
		}else if (this.eventDate.compareTo(another.getEventDate()) > 0){
			return 1;
		}else {
			return -1;
		}
	}
}