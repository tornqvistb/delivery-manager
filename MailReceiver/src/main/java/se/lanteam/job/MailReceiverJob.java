package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.service.MailReceiverService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class MailReceiverJob implements Job {
    @Autowired
    private MailReceiverService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.checkMails();
    }
}
