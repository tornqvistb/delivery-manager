package se.lanteam.model;

import java.util.Date;

public class DeliveryDay {

	public DeliveryDay(Date date, String dayOfWeek) {
		super();
		this.date = date;
		this.dayOfWeek = dayOfWeek;
	}
	private Date date;
	private String dayOfWeek;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
}
