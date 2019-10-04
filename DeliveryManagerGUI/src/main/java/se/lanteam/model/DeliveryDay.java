package se.lanteam.model;

import java.util.Date;

import se.lanteam.constants.DateUtil;

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
	public String getDateAsString() {
		if (date != null) {
			return DateUtil.dateToString(date);
		}
		return "";
	}
	public String getDayAndDate() {		
		if (date != null && dayOfWeek != null) {
			return dayOfWeek + " " + DateUtil.dateToString(date);
		}
		return "";
	}
	
}
