package se.lanteam.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date getDefaultStartDate() { 

		String dateString = "2010-01-01";
		return stringToDate(dateString);
	}

	public static Date getTomorrow() { 
		return new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
	}
	
	public static Date stringToDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();	
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
		}
	    
	    return date;
		
	}
}
