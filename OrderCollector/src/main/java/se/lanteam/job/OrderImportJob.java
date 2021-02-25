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
import se.lanteam.service.OrderPickImportService;
import se.lanteam.service.ShopOrderImportService;
import se.lanteam.services.ReportConsolidationService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
public class OrderImportJob implements Job {
    @Autowired
    private OrderImportService service;
    @Autowired
    private ErrorRepository errorRepo;
    @Autowired
    private ReportConsolidationService consolidationService;
    @Autowired
    private OrderPickImportService pickService;
    @Autowired
    private ShopOrderImportService shopService;

    private static final Logger LOG = LoggerFactory.getLogger(OrderImportJob.class);
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
    	LOG.info("Running OrderImportJob");
        try {
			//service.moveFiles();
			//consolidationService.updateAllReportLabels();
        	pickService.importFiles();
        	shopService.importFiles();
		} catch (IOException e) {
			e.printStackTrace();
			errorRepo.save(new ErrorRecord("IOException vid inläsning av filer från Visma."));
		} catch (Exception e) {
			errorRepo.save(new ErrorRecord("Exception vid inläsning av fil."));
		}
        service.addJointDeliveryInfo();
    }
}
