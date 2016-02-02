package se.lanteam.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.visma.Order;
import se.lanteam.visma.Orderrad;

/**
 * Created by david on 2015-01-20.
 */
@Service
public class OrderTransmitService {

	private static final String GENERAL_ERROR = "Fel vid överföring av orderleverans till kund. ";
	private static final String UNEXPECTED_ERROR = GENERAL_ERROR + "Ett oväntat fel uppstod.";
	private static final String FILE_EXPORT_ERROR = GENERAL_ERROR + "Ett fel uppstod när fil till Visma skulle lagras på disk, order: ";

	private static final Logger LOG = LoggerFactory.getLogger(OrderTransmitService.class);

	@Value("${file-transmit-folder}")
    private String fileTransmitFolder;
    
    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    
	public void transmitOrders() {
        LOG.info("Looking for orders to transmit!");
        List<OrderHeader> orders = orderRepo.findOrdersByStatus(StatusConstants.ORDER_STATUS_SENT);
        if (orders != null && orders.size() > 0) {
        	for (OrderHeader order : orders) {
        		// Create soap message and send to Intraservice
        		// Create message to Visma and store on disk
        		createFileToBusinessSystem(order);
            	// Update order status
        		order.setStatus(StatusConstants.ORDER_STATUS_TRANSFERED);
        		orderRepo.save(order);
        	}
        
        }
	}

	private void createFileToBusinessSystem(OrderHeader order) {
		Order vismaOrder = new Order();
		vismaOrder.setOrdernummer(Integer.parseInt(order.getOrderNumber()));
		List<Orderrad> vismaRows = new ArrayList<Orderrad>();
		for (OrderLine line : order.getOrderLines()) {
			Orderrad vismaRow = new Orderrad();
			vismaRow.setArtikelnummer(line.getArticleNumber());
			vismaRow.setOrderrad(line.getRowNumber());
			List<String> vismaSnr = new ArrayList<String>();
			for (Equipment equipment : line.getEquipments()) {
				vismaSnr.add(equipment.getSerialNo());        				
			}
			vismaRow.setSerienummer(vismaSnr);
			vismaRows.add(vismaRow);
		}
		vismaOrder.setOrderrader(vismaRows);
		Gson gson = new Gson();
		String json = gson.toJson(vismaOrder);
		try {
			FileWriter writer = new FileWriter(fileTransmitFolder + order.getOrderNumber() + ".json");
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			saveError(FILE_EXPORT_ERROR + order.getOrderNumber());
		}		
	}
	
    /*
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
	*/
	
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
