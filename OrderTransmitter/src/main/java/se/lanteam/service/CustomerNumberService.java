package se.lanteam.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.CustomerNumber;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.CustomerNumberRepository;
import se.lanteam.repository.OrderRepository;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class CustomerNumberService {


    private OrderRepository orderRepo;
    private CustomerGroupRepository custGroupRepo;
    private CustomerNumberRepository custNumberRepo;
    
    
    private static final Logger LOG = LoggerFactory.getLogger(CustomerNumberService.class);
    
	public void collectCustomerNumbers() {
        LOG.info("Collecting customer numbers!");
        List<CustomerGroup> groups = custGroupRepo.findAll();
		custNumberRepo.deleteAllRecords();
        for (CustomerGroup group : groups) {
        	List<OrderHeader> orders = orderRepo.findOrdersDistinctCustomer(group.getId());        	
        	Set<CustomerNumber> custNumbers = new HashSet<CustomerNumber>();
        	for (OrderHeader order : orders) {
        		custNumbers.add(new CustomerNumber(order.getCustomerNumber(), order.getCustomerName(), group));
        	}
        	group.setCustomerNumbers(custNumbers);
        	custGroupRepo.save(group);
        }
	}	
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setCustomerRepo(CustomerGroupRepository custGroupRepo) {
		this.custGroupRepo = custGroupRepo;
	}
	@Autowired
	public void setCustomerNumberRepo(CustomerNumberRepository custNumberRepo) {
		this.custNumberRepo = custNumberRepo;
	}
}
