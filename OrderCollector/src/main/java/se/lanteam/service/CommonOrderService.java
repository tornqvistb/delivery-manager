package se.lanteam.service;

import org.springframework.stereotype.Service;

import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class CommonOrderService {
			
    public OrderHeader doAutoRegistrationIfNeeded(OrderHeader order) {
    	if (order.getCustomerGroup() != null && order.getCustomerGroup().getAutoRegisterInternalOrderLines()) {
    		for (OrderLine ol : order.getOrderLines()) {
    			if (ol.getCustomerRowNumber() == 0) {
    				ol.setRegistered(ol.getTotal());
    				ol.setRemaining(0);
    				ol.setAutoRegistered(true);
    			}
    		}
    	}
    	return order;
    }
}
