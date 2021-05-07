package se.lanteam.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(MailReceiverJob.class);
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
    	LOG.info("Running MailReceiverJob");
    	// Don't read mails from mailbox anymore
    	// receiverService.checkMails();
    	senderService.checkMailsToSend();
    }
}
