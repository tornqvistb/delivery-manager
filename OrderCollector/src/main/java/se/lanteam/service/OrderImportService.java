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

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.OrderRepository;

/**
 * Created by david on 2015-01-20.
 */
@Service
public class OrderImportService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderImportService.class);
    @Value("${file-source-folder}") 
    private String fileSourceFolder;    
    @Value("${file-destination-folder}")
    private String fileDestFolder;
    @Value("${file-error-folder}")
    private String fileErrorFolder;
    
    private OrderRepository orderRepo;
    
    public void hello() {
        LOG.info("Hello World!");
        moveFiles();
    }
    
	private void moveFiles() {
		final File inputFolder = new File(fileSourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				System.out.println("FILE : " + fileEntry.getName());
				Path source = Paths.get(fileSourceFolder + "/" + fileEntry.getName());
				Path target = Paths.get(fileDestFolder + "/" + fileEntry.getName());
				try {
					StringBuilder sb = new StringBuilder();
					List<String> rows = Files.readAllLines(source);
					for (String row : rows) {
						sb.append(row);
					}
					OrderHeader orderHeader = getOrderHeaderFromJson(sb.toString());
					orderRepo.save(orderHeader);
					Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

    
	private OrderHeader getOrderHeaderFromJson(String json) {
		OrderHeader orderHeader = new OrderHeader();
		JSONObject jsonOrder = new JSONObject(json);
		orderHeader.setOrderNumber(String.valueOf(jsonOrder.getInt("Ordernummer")));
		orderHeader.setOrderDate(jsonDateToDate(jsonOrder.getString("Orderdatum")));
		//orderHeader.setCustomerName(jsonOrder.getString("Kundnamn"));
		orderHeader.setCustomerName("Saknas");
		orderHeader.setCustomerNumber(jsonOrder.getString("Kundnummer"));
		orderHeader.setPostalAddress1(jsonOrder.getString("Postadress1"));
		orderHeader.setPostalAddress2(jsonOrder.getString("Postadress2"));
		orderHeader.setPostalCode(jsonOrder.getString("Postnummer"));
		orderHeader.setCity(jsonOrder.getString("Ort"));
		orderHeader.setDeliveryPostalAddress1(jsonOrder.getString("Leveransadress_postadress1"));
		orderHeader.setDeliveryPostalAddress2(jsonOrder.getString("Leveransadress_postadress2"));
		orderHeader.setDeliveryPostalCode(jsonOrder.getString("Leveransadress_postnummer"));
		orderHeader.setDeliveryCity(jsonOrder.getString("Leveransadress_ort"));
		orderHeader.setLeasingNumber(jsonOrder.getString("Leasingavtal"));		
		orderHeader.setCustomerOrderNumber(jsonOrder.getString("Intraservice_ordernummer"));
		orderHeader.setCustomerSalesOrder(jsonOrder.getString("Intraservice_beställningsnummer"));
		orderHeader.setPartnerId(jsonOrder.getString("PartnerId"));
		orderHeader.setContact1Name(jsonOrder.getString("Kontakt1_namn"));
		orderHeader.setContact1Email(jsonOrder.getString("Kontakt1_epost"));
		orderHeader.setContact1Phone(jsonOrder.getString("Kontakt1_telefon"));
		orderHeader.setContact2Name(jsonOrder.getString("Kontakt2_namn"));
		orderHeader.setContact2Email(jsonOrder.getString("Kontakt2_epost"));
		orderHeader.setContact2Phone(jsonOrder.getString("Kontakt2_telefon"));
		orderHeader.setStatus(StatusConstants.ORDER_STATUS_NEW);
		JSONArray jsonOrderLines = jsonOrder.getJSONArray("Orderrader");
		for (int i = 0; i < jsonOrderLines.length(); i++) {			
			JSONObject jsonOrderLine = jsonOrderLines.getJSONObject(i);
			OrderLine orderLine = new OrderLine();
			orderLine.setRowNumber(jsonOrderLine.getInt("Radnummer"));
			orderLine.setArticleNumber(jsonOrderLine.getString("Artikelnummer"));
			orderLine.setArticleDescription(jsonOrderLine.getString("Artikelbenämning"));
			orderLine.setTotal(jsonOrderLine.getInt("Antal"));
			orderLine.setRemaining(jsonOrderLine.getInt("Antal"));
			orderLine.setRegistered(0);
			orderLine.setRestrictionCode(jsonOrderLine.getString("Restriktionskod"));
			orderLine.setOrderHeader(orderHeader);
			orderHeader.getOrderLines().add(orderLine);
		}
		return orderHeader;
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

	
}
