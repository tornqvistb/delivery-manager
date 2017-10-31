package se.lanteam.repository;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.lanteam.LimCommonsApplication;
import se.lanteam.constants.DateUtil;
import se.lanteam.domain.OrderHeader;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LimCommonsApplication.class)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepo;
	
	@Test
	public void testFindOrdersByPlanDate() {
		try {
			List<OrderHeader> orders = orderRepo.findOrdersByPlanDate(DateUtil.stringToDate("2017-02-13"), 1L);
			if (!orders.isEmpty()) {
				assert(true);
			} else {
				assert(false);
			}
		} catch (ParseException e) {
			assert(false);
		}	
	}
}
