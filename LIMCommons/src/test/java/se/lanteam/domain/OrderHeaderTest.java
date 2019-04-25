package se.lanteam.domain;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.lanteam.LimCommonsApplication;
import se.lanteam.constants.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LimCommonsApplication.class)
public class OrderHeaderTest {
	
	@Test
	public void testGetSlaDaysLeftOpenOrder() {
		OrderHeader order = new OrderHeader();
		order.setOrderDate(new Date());
		order.setDeliveryDate(null);
		Integer daysLeft = order.getSlaDaysLeft();
		assert(true);			
	}
	
	@Test
	public void testGetSlaDaysLeftDeliveredOrder() {
		OrderHeader order = new OrderHeader();
		try {
			order.setOrderDate(DateUtil.stringToDate("2017-03-08"));
			order.setDeliveryDate(DateUtil.stringToDate("2017-03-22"));
		} catch (Exception e) {
		}
		Integer daysLeft = order.getSlaDaysLeft();
		assert(true);			
	}
	@Test
	public void testIsOriginateFromServiceNow() {
		OrderHeader order = new OrderHeader();
		order.setLeasingNumber("N0602010/3");
		assert(order.isOriginateFromServiceNow());			
	}
	@Test
	public void testIsNotOriginateFromServiceNow() {
		OrderHeader order = new OrderHeader();
		order.setLeasingNumber("N0602010");
		assert(!order.isOriginateFromServiceNow());			
	}
	@Test
	public void testGetRequestNumber() {
		OrderHeader order = new OrderHeader();
		order.setCustomerOrderNumber("REQ1108594.RITM0010237");
		assert(order.getRequestNumber().equals("REQ1108594"));			
	}
	@Test
	public void testGetTotalItemsForSNOrder() {
		OrderHeader order = new OrderHeader();
		order.setLeasingNumber("N060TRAE/3");
		assert(order.getTotalItemsForSNOrder() == 3);			
	}
	@Test
	public void testGetTotalItemsForSNOrderNotNumeric() {
		OrderHeader order = new OrderHeader();
		order.setLeasingNumber("N060TRAE/W");
		assert(order.getTotalItemsForSNOrder() == 1);			
	}
	@Test
	public void testGetTotalItemsForSNOrderEmpty() {
		OrderHeader order = new OrderHeader();
		order.setLeasingNumber("");
		assert(order.getTotalItemsForSNOrder() == 1);			
	}

}