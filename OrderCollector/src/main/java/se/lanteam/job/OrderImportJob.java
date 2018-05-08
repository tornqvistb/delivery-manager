package se.lanteam.job;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.lanteam.domain.ErrorRecord;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.service.OrderImportService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class OrderImportJob implements Job {
    @Autowired
    private OrderImportService service;
    @Autowired
    private ErrorRepository errorRepo;

    private static final Logger LOG = LoggerFactory.getLogger(OrderImportJob.class);
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
    	LOG.info("Running OrderImportJob");
        try {
			service.moveFiles();
		} catch (IOException e) {
			e.printStackTrace();
			errorRepo.save(new ErrorRecord("IOException vid inläsning av filer från Visma."));
		}
        service.addJointDeliveryInfo();
    }
}
