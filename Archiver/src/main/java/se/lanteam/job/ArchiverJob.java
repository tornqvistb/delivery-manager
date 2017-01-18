package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.service.ArchiverService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class ArchiverJob implements Job {
    @Autowired
    private ArchiverService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.archiveOrders();
    }
}
