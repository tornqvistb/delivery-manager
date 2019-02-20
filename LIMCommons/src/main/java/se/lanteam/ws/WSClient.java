package se.lanteam.ws;

import java.net.MalformedURLException;

import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;

public class WSClient {
	
	public static final String WS_RETURN_CODE_OK = "0";
	
	public Header sendOrderDeliveryHamster(OrderHeader orderHeader, WSConfig config) throws MalformedURLException {
		return new LevAviseringHamsterClient().sendOrderDelivery(orderHeader, config);
	}

	public Header sendOrderDeliveryServiceNow(OrderHeader orderHeader, WSConfig config) throws MalformedURLException {
		return new LevAviseringServiceNowClient().sendOrderDelivery(orderHeader, config);
	}

	public Header sendDeliveryStatusHamster(OrderComment orderComment, WSConfig config) throws MalformedURLException {
		return new LevStatusHamsterClient().sendDeliveryStatus(orderComment, config);
	}
	
	public Header sendDeliveryStatusServiceNow(OrderComment orderComment, WSConfig config) throws MalformedURLException {
		return new LevStatusServiceNowClient().sendDeliveryStatus(orderComment, config);
	}
	
}
