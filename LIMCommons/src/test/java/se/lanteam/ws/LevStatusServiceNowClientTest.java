package se.lanteam.ws;

import org.junit.Test;

import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;

public class LevStatusServiceNowClientTest {

	@Test
	public void test() {
		LevStatusServiceNowClient client = new LevStatusServiceNowClient();
		OrderComment comment = new OrderComment();
		OrderHeader order = new OrderHeader();
		order.setOrderNumber("1000");
		order.setCustomerOrderNumber("1000");
		comment.setOrderHeader(order);
		comment.setId(1000L);
		comment.setMessage("Ett test");
		comment.setStatus("new");
		WSConfig config = new WSConfig("http://esb.goteborg.se/LanTeam_LevStatus", "LanTeam", "LanTeam2019");
		try {
			client.sendDeliveryStatus(comment, config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
