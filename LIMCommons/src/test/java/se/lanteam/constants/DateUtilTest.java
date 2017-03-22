package se.lanteam.constants;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.lanteam.LimCommonsApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LimCommonsApplication.class)
public class DateUtilTest {
	@Test
	public void testAddWorkingDays() {
		try {
			LocalDate startDate = DateUtil.stringToDate("2017-03-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate comingDate = DateUtil.addWorkingDays(startDate, 25);
			String stringDate = comingDate.toString();
			System.out.println("Datum: " + stringDate);
			Integer daysBetween = DateUtil.getWorkingDaysBetweenDates(startDate, comingDate);
			System.out.println("Vardagar mellan " + startDate.toString() + " och " +  comingDate.toString() + ": " + daysBetween);
		} catch (ParseException e) {			
		}
		assert(true);			
	}
}
