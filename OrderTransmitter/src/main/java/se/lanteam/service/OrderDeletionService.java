package se.lanteam.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.OrderRepository;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
@Transactional
public class OrderDeletionService {


    private OrderRepository orderRepo;
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderDeletionService.class);
    
	public void deleteOrders() {
        LOG.debug("Looking for orders to delete!");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -48);
        Date theDate = cal.getTime();
        List<OrderHeader> orders = orderRepo.findOrdersByCreationDateAndStatus(StatusConstants.ORDER_STATUS_RECEIVING, theDate);
        for (OrderHeader order : orders) {        	
        	LOG.info("Deleting order with status receiving after 48 hours: " + order.getOrderNumber());
        	orderRepo.delete(order.getId());
        }
	}

	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
}
