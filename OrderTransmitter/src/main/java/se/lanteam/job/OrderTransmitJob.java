package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.service.OrderDeletionService;
import se.lanteam.service.OrderTransmitService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class OrderTransmitJob implements Job {
    @Autowired
    private OrderTransmitService service;
    @Autowired
    private OrderDeletionService deletionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.transmitOrders();
        service.transmitOrderComments();
        deletionService.deleteOrders();
    }
}
