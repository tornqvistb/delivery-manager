package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.service.MailReceiverService;
import se.lanteam.service.MailSenderService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class MailReceiverJob implements Job {
    @Autowired
    private MailReceiverService receiverService;
    @Autowired
    private MailSenderService senderService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
    	receiverService.checkMails();
    	senderService.checkMailsToSend();
    }
}
