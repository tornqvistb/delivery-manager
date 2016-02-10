package se.lanteam.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import header.common.esb.staden._1.Header;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderCommentRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PropertyService;
import se.lanteam.visma.Order;
import se.lanteam.visma.Orderrad;
import se.lanteam.ws.WSClient;
import se.lanteam.ws.WSConfig;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class OrderTransmitService {

	private static final String GENERAL_ERROR = "Fel vid överföring av orderleverans till kund. ";
	private static final String FILE_EXPORT_ERROR = GENERAL_ERROR + "Ett fel uppstod när fil till Visma skulle lagras på disk, order: ";
	private static final String WS_ERROR = GENERAL_ERROR + "Ett fel uppstod när leveransrapportering mot Intraservice: ";

	private static final Logger LOG = LoggerFactory.getLogger(OrderTransmitService.class);

    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    private OrderCommentRepository orderCommentRepo;
    private PropertyService propService;
    
	public void transmitOrders() {
		String wsEndpointOrderDelivery = propService.getString(PropertyConstants.WS_ENDPOINT_ORDER_DELIVERY);
		String wsUserName = propService.getString(PropertyConstants.WS_USERNAME_GBCA);
		String wsPassword = propService.getString(PropertyConstants.WS_PASSWORD_GBCA);
        LOG.info("Looking for orders to transmit!");
        List<OrderHeader> orders = orderRepo.findOrdersByStatus(StatusConstants.ORDER_STATUS_SENT);
		WSConfig config = new WSConfig(wsEndpointOrderDelivery, wsUserName, wsPassword);
		WSClient wsClient = new WSClient();
        if (orders != null && orders.size() > 0) {
        	for (OrderHeader order : orders) {
        		// Create soap message and send to Intraservice
        		try {
					Header header = wsClient.sendOrderDelivery(order, config);
					if (!WSClient.WS_RETURN_CODE_OK.equals(header.getKod())) {
						throw new Exception(header.getKod() + " - " + header.getText());
					}
					// Create message to Visma and store on disk
					createFileToBusinessSystem(order);
					// Update order status
					order.setStatus(StatusConstants.ORDER_STATUS_TRANSFERED);
					orderRepo.save(order);
				} catch (Exception e) {
					saveError(WS_ERROR + e.getMessage());
					e.printStackTrace();
				}
        	}        
        }
	}

	private void createFileToBusinessSystem(OrderHeader order) {
		String fileTransmitFolder = propService.getString(PropertyConstants.FILE_TRANSMIT_FOLDER);
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
		
	public void transmitOrderComments() {
        LOG.info("Looking for orders to transmit!");
		String wsEndpointOrderComment = propService.getString(PropertyConstants.WS_ENDPOINT_ORDER_COMMENT);
		String wsUserName = propService.getString(PropertyConstants.WS_USERNAME_GBCA);
		String wsPassword = propService.getString(PropertyConstants.WS_PASSWORD_GBCA);
        List<OrderComment> orderComments = orderCommentRepo.findOrderCommentsByStatus(StatusConstants.ORDER_STATUS_NEW);
		WSConfig config = new WSConfig(wsEndpointOrderComment, wsUserName, wsPassword);
		WSClient wsClient = new WSClient();
        if (orderComments != null && orderComments.size() > 0) {
        	for (OrderComment comment : orderComments) {
        		// Create soap message and send to Intraservice
        		try {
					Header header = wsClient.sendDeliveryStatus(comment, config);
					if (!WSClient.WS_RETURN_CODE_OK.equals(header.getKod())) {
						throw new Exception(header.getKod() + " - " + header.getText());
					}
					// Update order comment status
					comment.setStatus(StatusConstants.ORDER_STATUS_TRANSFERED);
					orderCommentRepo.save(comment);
				} catch (Exception e) {
					saveError(WS_ERROR + e.getMessage());
					e.printStackTrace();
				}
        	}        
        }
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
	@Autowired
	public void setOrderCommentRepo(OrderCommentRepository orderCommentRepo) {
		this.orderCommentRepo = orderCommentRepo;
	}
	@Autowired
	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}
	
}
