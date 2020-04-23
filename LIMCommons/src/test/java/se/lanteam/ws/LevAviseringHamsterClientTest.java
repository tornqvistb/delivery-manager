package se.lanteam.ws;

import org.junit.Test;

import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;

public class LevAviseringHamsterClientTest {

	@Test
	public void test() {
		LevAviseringHamsterClient client = new LevAviseringHamsterClient();
		OrderHeader order = new OrderHeader();
		order.setOrderNumber("1000");
		order.setCustomerOrderNumber("1000");
		WSConfig config = new WSConfig("http://esb.goteborg.se/Wsdl/GBCA003A_LeveransAvisering_https_.wsdl", "GBCALanTeamProd", "alsdKf21");
		
		try {
			client.sendOrderDelivery(order, config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
