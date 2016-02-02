package se.lanteam.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;

/**
 * Created by david on 2015-01-20.
 */
@Service
public class MailReceiverService {

	private static final String GENERAL_FILE_ERROR = "Fel vid inläsning av fil. ";
	private static final String ERROR_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Ordernummer saknas i fil: ";
	private static final String ERROR_CUSTOMER_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Kundens ordernummer saknas i fil: ";
	private static final String ERROR_NO_ORDER_LINES = GENERAL_FILE_ERROR + "Kundens ordernummer saknas i fil: ";
	private static final String ERROR_ARTICLE_ID_MISSING = GENERAL_FILE_ERROR + "Artikel-ID saknas på orderrad i fil: ";
	private static final String ERROR_ROW_NUMBER_MISSING = GENERAL_FILE_ERROR + "Orderradnummer saknas på orderrad i fil: ";
	private static final String ERROR_TOTAL_MISSING = GENERAL_FILE_ERROR + "Antal saknas på orderrad i fil: ";
	private static final String ERROR_RESTRICTION_CODE_MISSING = GENERAL_FILE_ERROR + "Restriktionskod saknas på orderrad i fil: ";
	private static final String ERROR_ORDER_NUMBER_ALREADY_EXISTS = GENERAL_FILE_ERROR + "Ordernummer finns redan, fil: ";
	
	private static final Logger LOG = LoggerFactory.getLogger(MailReceiverService.class);
    @Value("${file-source-folder}") 
    private String fileSourceFolder;    
    @Value("${file-destination-folder}")
    private String fileDestFolder;
    @Value("${file-error-folder}")
    private String fileErrorFolder;
    
    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    
    public void hello() {
        LOG.info("Hello World!");
        moveFiles();
    }
    
	
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}

	
}
