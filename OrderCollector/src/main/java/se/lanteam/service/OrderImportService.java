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
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class OrderImportService {

	private static final String GENERAL_FILE_ERROR = "Fel vid inläsning av fil. ";
	private static final String ERROR_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Ordernummer saknas i fil: ";
	private static final String ERROR_CUSTOMER_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Kundens ordernummer saknas i fil: ";
	private static final String ERROR_NO_ORDER_LINES = GENERAL_FILE_ERROR + "Kundens ordernummer saknas i fil: ";
	private static final String ERROR_ARTICLE_ID_MISSING = GENERAL_FILE_ERROR + "Artikel-ID saknas på orderrad i fil: ";
	private static final String ERROR_ROW_NUMBER_MISSING = GENERAL_FILE_ERROR + "Orderradnummer saknas på orderrad i fil: ";
	private static final String ERROR_TOTAL_MISSING = GENERAL_FILE_ERROR + "Antal saknas på orderrad i fil: ";
	private static final String ERROR_RESTRICTION_CODE_MISSING = GENERAL_FILE_ERROR + "Restriktionskod saknas på orderrad i fil: ";
	private static final String ERROR_ORDER_NUMBER_ALREADY_EXISTS = GENERAL_FILE_ERROR + "Ordernummer finns redan, fil: ";
	
	private static final Logger LOG = LoggerFactory.getLogger(OrderImportService.class);
    @Value("${file-source-folder}") 
    private String fileSourceFolder;    
    @Value("${file-destination-folder}")
    private String fileDestFolder;
    @Value("${file-error-folder}")
    private String fileErrorFolder;
    
    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    
	public void moveFiles() {
        LOG.info("Looking for files to move!");
		final File inputFolder = new File(fileSourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				System.out.println("FILE : " + fileEntry.getName());
				Path source = Paths.get(fileSourceFolder + "/" + fileEntry.getName());
				Path target = Paths.get(fileDestFolder + "/" + fileEntry.getName());
				Path errorTarget = Paths.get(fileErrorFolder + "/" + fileEntry.getName());
				try {
					StringBuilder sb = new StringBuilder();
					List<String> rows = Files.readAllLines(source);
					for (String row : rows) {
						sb.append(row);
					}
					OrderHeader orderHeader = getOrderHeaderFromJson(sb.toString());
					if (validate(orderHeader, fileEntry.getName())) {
						OrderComment comment = new OrderComment();
						comment.setMessage("Tack för din beställning!");
						comment.setCreationDate(new Date());
						comment.setOrderLine("0");
						orderHeader.getOrderComments().add(comment);
						orderRepo.save(orderHeader);
						Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
					} else {
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

    
	private OrderHeader getOrderHeaderFromJson(String json) {
		OrderHeader orderHeader = new OrderHeader();
		JSONObject jsonOrder = new JSONObject(json);
		orderHeader.setOrderNumber(String.valueOf(jsonOrder.optInt("Ordernummer")));
		orderHeader.setOrderDate(jsonDateToDate(jsonOrder.optString("Orderdatum")));
		orderHeader.setCustomerName(jsonOrder.optString("Kundnamn"));
		orderHeader.setCustomerNumber(jsonOrder.optString("Kundnummer"));
		orderHeader.setPostalAddress1(jsonOrder.optString("Postadress1"));
		orderHeader.setPostalAddress2(jsonOrder.optString("Postadress2"));
		orderHeader.setPostalCode(jsonOrder.optString("Postnummer"));
		orderHeader.setCity(jsonOrder.optString("Ort"));
		orderHeader.setDeliveryPostalAddress1(jsonOrder.optString("Leveransadress_postadress1"));
		orderHeader.setDeliveryPostalAddress2(jsonOrder.optString("Leveransadress_postadress2"));
		orderHeader.setDeliveryPostalCode(jsonOrder.optString("Leveransadress_postnummer"));
		orderHeader.setDeliveryCity(jsonOrder.optString("Leveransadress_ort"));
		orderHeader.setLeasingNumber(jsonOrder.optString("Leasingavtal"));		
		orderHeader.setCustomerOrderNumber(jsonOrder.optString("Intraservice_ordernummer"));
		orderHeader.setCustomerSalesOrder(jsonOrder.optString("Intraservice_beställningsnummer"));
		orderHeader.setPartnerId(jsonOrder.optString("PartnerId"));
		orderHeader.setContact1Name(jsonOrder.optString("Kontakt1_namn"));
		orderHeader.setContact1Email(jsonOrder.optString("Kontakt1_epost"));
		orderHeader.setContact1Phone(jsonOrder.optString("Kontakt1_telefon"));
		orderHeader.setContact2Name(jsonOrder.optString("Kontakt2_namn"));
		orderHeader.setContact2Email(jsonOrder.optString("Kontakt2_epost"));
		orderHeader.setContact2Phone(jsonOrder.optString("Kontakt2_telefon"));
		orderHeader.setStatus(StatusConstants.ORDER_STATUS_NEW);
		JSONArray jsonOrderLines = jsonOrder.getJSONArray("Orderrader");
		for (int i = 0; i < jsonOrderLines.length(); i++) {			
			JSONObject jsonOrderLine = jsonOrderLines.getJSONObject(i);
			OrderLine orderLine = new OrderLine();
			orderLine.setRowNumber(jsonOrderLine.optInt("Radnummer"));
			orderLine.setArticleNumber(jsonOrderLine.optString("Artikelnummer"));
			orderLine.setArticleDescription(jsonOrderLine.optString("Artikelbenämning"));
			orderLine.setTotal(jsonOrderLine.optInt("Antal"));
			orderLine.setRemaining(jsonOrderLine.optInt("Antal"));
			orderLine.setRegistered(0);
			orderLine.setRestrictionCode(jsonOrderLine.optString("Restriktionskod"));
			orderLine.setOrderHeader(orderHeader);
			orderHeader.getOrderLines().add(orderLine);
		}
		return orderHeader;
	}
	
	private boolean validate(OrderHeader orderHeader, String fileName) {
		if (orderHeader.getOrderNumber().equals("0")) {
			saveError(ERROR_ORDER_NUMBER_MISSING + fileName);
			return false;
		}
		if (StringUtils.isEmpty(orderHeader.getCustomerOrderNumber())) {
			saveError(ERROR_CUSTOMER_ORDER_NUMBER_MISSING + fileName);
			return false;
		}
		if (orderHeader.getOrderLines().isEmpty()) {
			saveError(ERROR_NO_ORDER_LINES + fileName);
			return false;
		} else {
			for (OrderLine line : orderHeader.getOrderLines()) {
				if (StringUtils.isEmpty(line.getArticleNumber())) {
					saveError(ERROR_ARTICLE_ID_MISSING + fileName);
					return false;					
				}
				if (StringUtils.isEmpty(line.getRowNumber())) {
					saveError(ERROR_ROW_NUMBER_MISSING + fileName);
					return false;					
				}
				if (StringUtils.isEmpty(line.getTotal())) {
					saveError(ERROR_TOTAL_MISSING + fileName);
					return false;					
				}
				if (StringUtils.isEmpty(line.getRestrictionCode())) {
					saveError(ERROR_RESTRICTION_CODE_MISSING + fileName);
					return false;					
				}				
			}
		}
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderHeader.getOrderNumber());
		if (orders != null && orders.size() > 0) {
			saveError(ERROR_ORDER_NUMBER_ALREADY_EXISTS + fileName);
			return false;								
		}
		return true;
	}
	
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}
	
	private Date jsonDateToDate(String jsonDate)
	{
	    //  "/Date(1321867151710)/"
	    int idx1 = jsonDate.indexOf("(");
	    int idx2 = jsonDate.indexOf(")");
	    String s = jsonDate.substring(idx1+1, idx2);
	    long l = Long.valueOf(s);
	    return new Date(l);
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
