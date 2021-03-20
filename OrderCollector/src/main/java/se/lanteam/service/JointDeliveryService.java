package se.lanteam.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.constants.CustomFieldConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.OrderCustomFieldRepository;
import se.lanteam.repository.OrderRepository;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2021-02
 */
@Service
public class JointDeliveryService {
		
	private static final Logger LOG = LoggerFactory.getLogger(JointDeliveryService.class);

	@Autowired
    private OrderRepository orderRepo;
	@Autowired
    private OrderCustomFieldRepository orderCustomFieldRepo;
        
    public void addJointDeliveryInfo() {
    	List<OrderHeader> unJoinedOrders = orderRepo.findOrdersJointDeliveryUnjoined(StatusConstants.ORDER_STATUS_NOT_PICKED);
    	for (OrderHeader order : unJoinedOrders) {
    		if (CustomFieldConstants.VALUE_SAMLEVERANS_MASTER.equalsIgnoreCase(order.getJointDelivery())) {
    			// Master order
				List<OrderHeader> childOrders = orderRepo.findOrdersByJointDeliveryAndStatus(order.getNetsetOrderNumber(), StatusConstants.ORDER_STATUS_NOT_PICKED);
				if (childOrders.size() > 0) {
					StringBuffer orders = new StringBuffer();
					boolean first = true;
					for (OrderHeader childOrder : childOrders) {
						if (!first)
							orders.append(",");
						orders.append(childOrder.getOrderNumber());
						first = false;
					}
					order.setJointDeliveryOrders(orders.toString());
					order.setJointDeliveryText(String.format(CustomFieldConstants.TEXT_SAMLEVERANS_MASTER, orders.toString()));
					orderRepo.save(order);
				}
    		} else {
    			// Child order
    			List<OrderHeader> masterOrders = orderRepo.findOrdersByNetsetOrderNumberAndStatus(order.getJointDelivery(), StatusConstants.ORDER_STATUS_NOT_PICKED);
    			if (masterOrders.size() > 0) {
    				    				
    				LOG.debug("child, master-order-id: " + masterOrders.get(0).getId());
    				    				
    				OrderHeader masterOrder = orderRepo.findOne(masterOrders.get(0).getId());
    				String masterOrderNr = masterOrder.getOrderNumber();
					order.setJointDeliveryText(String.format(CustomFieldConstants.TEXT_SAMLEVERANS_CHILD, masterOrderNr));
					order.setJointDeliveryOrders(masterOrderNr);
					order.setExcludeFromList(true);
					orderRepo.save(order);
					/*
					for (OrderCustomField field : order.getOrderCustomFields()) {
						orderCustomFieldRepo.delete(field.getId());
					}
					order.setOrderCustomFields(null);
					Set<OrderCustomField> updatedCustomFields = new HashSet<OrderCustomField>();
					for (OrderCustomField customField : masterOrder.getOrderCustomFields()) {
						LOG.debug("child, customField " + customField.getId() + ", " + customField.getValue());
						String value = customField.getValue();
						if (customField.getCustomField().getIdentification() == CustomFieldConstants.CUSTOM_FIELD_JOINT_DELIVERY) {
							value = masterOrder.getNetsetOrderNumber();
						}
						updatedCustomFields.add(new OrderCustomField(customField.getCustomField(), order, value, customField.getCreationDate()));
					}					
					order.setOrderCustomFields(updatedCustomFields);
					order.setContact1Email(masterOrder.getContact1Email());
					order.setContact1Name(masterOrder.getContact1Name());
					order.setContact1Phone(masterOrder.getContact1Phone());
					*/
    			}
    		}    		
    	}    	
    }
}
