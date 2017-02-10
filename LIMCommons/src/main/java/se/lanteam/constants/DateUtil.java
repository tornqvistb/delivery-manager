package se.lanteam.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date NVL(Date inDate, Date replDate) {
		Date result = inDate;
		if (inDate == null) {
			result = replDate;
		}
		return result;
	}

	public static String getDefaultStartDateAsString() { 

		return "2010-01-01";
	}
	
	public static Date getDefaultStartDate() { 

		String dateString = "2010-01-01";
		Date result = new Date();
		try {
			result = stringToDate(dateString);
		} catch (ParseException e) {			
		}
		return result;
	}

	public static String getOneYearAheadAsString() { 
		return dateToString(addDaysToDate(new Date(), 365));
	}

	
	public static Date getOneYearAhead() { 
		return addDaysToDate(new Date(), 365);
	}
	
	public static Date getTomorrow() { 
		return new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
	}
	
	public static Date stringToDate(String dateString) throws ParseException  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();	
		date = formatter.parse(dateString);
	    
	    return date;
		
	}
	public static Date stringToDateMidnight(String dateString) throws ParseException  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();	
		date = formatter.parse(dateString + " 23:59");
	    
	    return date;
		
	}

	public static String dateToString(Date date) {
		
		if (date == null) return ""; 
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = formatter.format(date);
	    
	    return dateStr;
		
	}
	
	public static Date getStartDateForInactiveOrders(int daysBack) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());		
		cal.add(Calendar.DATE, - daysBack);
		Date startDate = cal.getTime(); 
		return startDate;
	}

	public static Date getNextDateByWeekday(Date startDate, int weekDay) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);		
		
		while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
			calendar.add(Calendar.DATE, 1);
		}
		
		return calendar.getTime();
	}

	public static Date addDaysToDate(Date date, int days) { 
		return new Date(date.getTime() + (days * 1000 * 60 * 60 * 24));
	}
	
}
