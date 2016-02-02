package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.service.MailReceiverService;

/**
 * Created by david on 2015-01-20.
 */
public class MailReceiverJob implements Job {
    @Autowired
    private MailReceiverService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.hello();
    }
}
