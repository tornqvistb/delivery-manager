package se.lanteam.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import se.lanteam.ws.netset.CreateOrderRequest;
import se.lanteam.ws.netset.CreateOrderResponse;



@Endpoint
public class NetsetOrderEndpoint {
	private static final String NAMESPACE_URI = "http://lanteam.se/ws/netset";
	private NetsetOrderRepository netsetOrderRepository;

	@Autowired
	public NetsetOrderEndpoint(NetsetOrderRepository netsetOrderRepository) {
		this.netsetOrderRepository = netsetOrderRepository;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "createOrderRequest")
	@ResponsePayload
	public CreateOrderResponse getResponse(@RequestPayload CreateOrderRequest orderRequest) {
		return netsetOrderRepository.createOrder(orderRequest);
	}

}
