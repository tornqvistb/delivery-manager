package se.lanteam.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date getDefaultStartDate() { 

		String dateString = "2010-01-01";
		Date result = new Date();
		try {
			result = stringToDate(dateString);
		} catch (ParseException e) {			
		}
		return result;
	}

	public static Date getTomorrow() { 
		return new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
	}
	
	public static Date stringToDate(String dateString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();	
		date = formatter.parse(dateString);
	    
	    return date;
		
	}
}
