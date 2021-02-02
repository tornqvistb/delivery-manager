package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.lanteam.domain.OrderHeader;

public class DeliveryFormTest {
	@Test
	public void testToAddOrder() {
		DeliveryForm form = new DeliveryForm();
		form.addOrderNumber("12345");
		form.addOrderNumber("23456");
				
		assert(form.getOrderNumbersConcat().equals("12345" + DeliveryForm.ORDER_SEPARATOR + "23456") 
				&& form.getOrdersAsList().size() == 2
				&& form.getOrdersAsList().get(0).equals("12345")
				&& form.getOrdersAsList().get(1).equals("23456"));			
	}

	@Test
	public void testToRemoveOrder() {
		DeliveryForm form = new DeliveryForm();
		form.addOrderNumber("12345");
		form.addOrderNumber("23456");

		form.removeOrderNumber("12345");
		
		assert(form.getOrderNumbersConcat().equals("23456") 
				&& form.getOrdersAsList().size() == 1
				&& form.getOrdersAsList().get(0).equals("23456"));			
	}

	@Test
	public void testToRemoveLastOrder() {
		DeliveryForm form = new DeliveryForm();
		form.addOrderNumber("12345");
		form.addOrderNumber("23456");

		form.removeOrderNumber("12345");
		form.removeOrderNumber("23456");
		
		assert(form.getOrderNumbersConcat().equals("") 
				&& form.getOrdersAsList().size() == 0);			
	}

	@Test
	public void testToAddReceiversOneOrder() {
		DeliveryForm form = new DeliveryForm();		
		OrderHeader oh = new OrderHeader();
		oh.setContact1Name("Kalle Kula");
		oh.setContact2Name("Pelle Boll");
		List<OrderHeader> orders = new ArrayList<>();
		orders.add(oh);
		form.setReceiversFromOrderList(orders);
		assert(form.getReceiverList().size() == 3);	
	}

	@Test
	public void testToAddReceiversManyOrdersSameContacts() {
		DeliveryForm form = new DeliveryForm();
		OrderHeader oh1 = new OrderHeader();
		oh1.setContact1Name("Kalle Kula");
		oh1.setContact2Name("Pelle Boll");
		OrderHeader oh2 = new OrderHeader();
		oh2.setContact1Name("Kalle Kula");
		oh2.setContact2Name("Pelle Boll");
		List<OrderHeader> orders = new ArrayList<>();
		orders.add(oh1);
		orders.add(oh2);
		form.setReceiversFromOrderList(orders);
		assert(form.getReceiverList().size() == 3);	
	}

	@Test
	public void testToAddReceiversManyOrdersDifferentContacts() {
		DeliveryForm form = new DeliveryForm();
		OrderHeader oh1 = new OrderHeader();
		oh1.setContact1Name("Kalle Kula");
		oh1.setContact2Name("Pelle Boll");
		OrderHeader oh2 = new OrderHeader();
		oh2.setContact1Name("Kalle Kula");
		oh2.setContact2Name("Pelle Bollträ");
		List<OrderHeader> orders = new ArrayList<>();
		orders.add(oh1);
		orders.add(oh2);
		form.setReceiversFromOrderList(orders);
		assert(form.getReceiverList().size() == 1);	
	}

	@Test
	public void testToAddReceiversManyOrdersSameContacts1And2Same() {
		DeliveryForm form = new DeliveryForm();
		OrderHeader oh1 = new OrderHeader();
		oh1.setContact1Name("Kalle Kula");
		oh1.setContact2Name("Kalle Kula");
		OrderHeader oh2 = new OrderHeader();
		oh2.setContact1Name("Kalle Kula");
		oh2.setContact2Name("Kalle Kula");
		List<OrderHeader> orders = new ArrayList<>();
		orders.add(oh1);
		orders.add(oh2);
		form.setReceiversFromOrderList(orders);
		assert(form.getReceiverList().size() == 2);	
	}

}
