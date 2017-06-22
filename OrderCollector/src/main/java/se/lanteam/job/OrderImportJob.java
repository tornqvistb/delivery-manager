package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.service.OrderImportService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class OrderImportJob implements Job {
    @Autowired
    private OrderImportService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.moveFiles();
        service.addJointDeliveryInfo();
    }
}
