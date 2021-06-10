package se.lanteam.job;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.repository.ErrorRepository;
import se.lanteam.service.JointDeliveryService;
import se.lanteam.service.OrderPickImportService;
import se.lanteam.service.ShopOrderImportService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class OrderImportJob implements Job {
    @Autowired
    private OrderPickImportService pickService;
    @Autowired
    private ShopOrderImportService shopService;
    @Autowired
    private JointDeliveryService jointDeliveryService;

    private static final Logger LOG = LoggerFactory.getLogger(OrderImportJob.class);
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
    	LOG.info("Running OrderImportJob");
        try {
        	shopService.importFiles();
        	jointDeliveryService.addJointDeliveryInfo();
        	pickService.importFiles();
        	
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
    }
}
