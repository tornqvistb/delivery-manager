package se.lanteam.service;

import static se.lanteam.model.OrderPickingInfo.FIELD_SEPARATOR;
import static se.lanteam.model.OrderPickingInfo.ROW_TYPE_HEADER;
import static se.lanteam.model.OrderPickingInfo.ROW_TYPE_LINE;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.exceptions.PickImportException;
import se.lanteam.helpers.OrderCloneHelper;
import se.lanteam.model.OrderPickingInfo;
import se.lanteam.model.PickedOrderLine;
import se.lanteam.model.RestOrder;
import se.lanteam.model.RestOrderLine;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PropertyService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2021-02
 */
@Service
public class OrderPickImportService {

	
	private static final Logger logger = LoggerFactory.getLogger(OrderPickImportService.class);

	@Autowired
    private OrderRepository orderRepo;
	@Autowired
    private ErrorRepository errorRepo;
	@Autowired
    private PropertyService propService;

	private static final String FILE_ENDING_WH = ".txt";
	private final static String ERROR_WHEN_RECEIVNING_PICKING_FILE = "Fel uppstod vid inläsning av plock-fil från Lexit: ";
	private final static String ERROR_WHEN_RECEIVNING_RESTORDER_FILE = "Fel uppstod vid inläsning av restorder-fil från Lexit: ";
	private final static String ERROR_UNKNOWN_ORDER = ERROR_WHEN_RECEIVNING_PICKING_FILE + "Order finns ej i LIM: ";
	private final static String PREFIX_REST_ORDER = "RO_";
	
	@Transactional
    public void importFiles() throws IOException{
	    String fileSourceFolder = propService.getString(PropertyConstants.FILE_INCOMING_WH_FOLDER);	    
	    String fileDestFolder = propService.getString(PropertyConstants.FILE_PROCESSED_WH_FOLDER);	    
	    String fileErrorFolder = propService.getString(PropertyConstants.FILE_ERROR_WH_FOLDER);	            
		final File inputFolder = new File(fileSourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				if (fileNamePatternMatches(fileEntry.getName())) {
					logger.info("Found file: " + fileEntry.getName());
					Path source = Paths.get(fileSourceFolder + "/" + fileEntry.getName());
					Path target = Paths.get(fileDestFolder + "/" + fileEntry.getName());
					Path errorTarget = Paths.get(fileErrorFolder + "/" + fileEntry.getName());
					try {
						List<String> rows = Files.readAllLines(source, StandardCharsets.ISO_8859_1);
						if (fileEntry.getName().startsWith(PREFIX_REST_ORDER)) {
							RestOrder restOrder = getRestOrder(rows, fileEntry.getName());
							createRestOrder(restOrder);
						} else {
							OrderPickingInfo pickingInfo = getPickingInfo(rows, fileEntry.getName());						
							updateOrder(pickingInfo);						
							logger.info(pickingInfo.toString());
						}
						Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
					} catch (PickImportException e) {
						errorRepo.save(new ErrorRecord(e.getMessage()));
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						errorRepo.save(new ErrorRecord(ERROR_WHEN_RECEIVNING_PICKING_FILE + fileEntry.getName()));						
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}
    }
    
    private boolean fileNamePatternMatches(String fileName) {
    	return fileName != null && fileName.endsWith(FILE_ENDING_WH);
    }
	
    private void createRestOrder(RestOrder restOrder) {
    	List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(restOrder.getOriginalOrderNumber());
    	if (!orderList.isEmpty()) {
    		OrderHeader originalOrder = orderList.get(0);
    		OrderHeader restOrderEntity = OrderCloneHelper.cloneOrder(originalOrder, restOrder);
    		orderRepo.save(restOrderEntity);
    	}    	
    }	
	
    private void updateOrder(OrderPickingInfo pickingInfo) throws PickImportException {
    	List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(pickingInfo.getOrderNumber());
    	if (!orderList.isEmpty()) {
    		OrderHeader order = orderList.get(0);
    		//Check status, maybe change this condition later!!
    		if (order.getEditable()) {
    			
    			for (PickedOrderLine pickedLine : pickingInfo.getPickedLines()) {
    				updateFirstMatchingOrderLine(order, pickedLine);
    			}    			
    		}
    		order.setPickStatus(pickingInfo.getStatus());
    		updateOrderStatusByPickStatus(order);
    		orderRepo.save(order);
    	} else {
    		throw new PickImportException(ERROR_UNKNOWN_ORDER + pickingInfo.getOrderNumber());
    	}
    }
    
    private void updateFirstMatchingOrderLine(OrderHeader order, PickedOrderLine pickedLine) {
    	for (OrderLine line : order.getOrderLines()) {
    		if (line.getArticleNumber().equals(pickedLine.getArticleId())) {
    			if (line.isFullyRegistered() && pickedLine.getAmount() > 0) {
    				continue;
    			}
    			if (pickedLine.getSerialNumbers().isEmpty() && !line.isFullyRegistered()) {
    				line.addPickedQuantity(pickedLine.getAmount());
    				break;
    			} else {
    				if (pickedLine.getAmount() > 0 && !line.isFullyRegistered()) {
						line.addPickedSerialNumbers(pickedLine.getSerialNumbers());
						break;
					} else {
						if (removeEquipments(line, pickedLine.getSerialNumbers())) {
							break;
						}
					}    				
    			}
    		}
    	}
    }

    private boolean removeEquipments(OrderLine line, List<String> serialNumbers) {
    	boolean anyRemoved = false;
		for (Iterator<Equipment> iterator = line.getEquipments().iterator(); iterator.hasNext();) {					
			Equipment equipment = iterator.next();
			if (serialNumbers.contains(equipment.getSerialNo())) {
				equipment.setOrderLine(null);
				iterator.remove();
				line.updateEquipmentCounters();
				anyRemoved = true;
			}
		}
		return anyRemoved;
    }

    
    private void updateOrderStatusByPickStatus(OrderHeader order) {
    	if (StatusConstants.PICK_STATUS_PARTLY_PICKED == order.getPickStatus() 
    			&& (StatusConstants.ORDER_STATUS_NOT_PICKED.equals(order.getStatus()) || StatusConstants.ORDER_STATUS_DELIVERY_ERROR.equals(order.getStatus()))) {
    		order.setStatus(StatusConstants.ORDER_STATUS_STARTED);
    	}
    	if (StatusConstants.PICK_STATUS_FULLY_PICKED == order.getPickStatus()) {
    		boolean manualRegistrationLeft = false;
    		List<OrderLine> uncompletedLines = order.getUnCompletedOrderLines();
    		Set<OrderLine> allLines = order.getOrderLines();
    		for (OrderLine line : allLines) {
    			if (line.isManualRegistrationLeft()) {
    				manualRegistrationLeft = true;
    			}
    			for (OrderLine ucLine : uncompletedLines) {
    				if (ucLine.getRowNumber() == line.getRowNumber()) {
    					line.setRested(true);
    				}
    			}
    		}
    		if (!manualRegistrationLeft) {
    			if (StatusConstants.ORDER_STATUS_BOOKED.equals(order.getStatus())) {
    				order.setStatus(StatusConstants.ORDER_STATUS_ROUTE_PLANNED);
    			} else {
    				order.setStatus(StatusConstants.ORDER_STATUS_REGISTRATION_DONE);
    			}
    		}
    		if (manualRegistrationLeft 
    				&& (StatusConstants.ORDER_STATUS_NOT_PICKED.equals(order.getStatus()) || StatusConstants.ORDER_STATUS_DELIVERY_ERROR.equals(order.getStatus()))) {
    			order.setStatus(StatusConstants.ORDER_STATUS_STARTED);
    		}
    	}
    }
 
    private RestOrder getRestOrder(List<String> rows, String fileName) throws PickImportException{
    	try {
    		RestOrder restOrder = new RestOrder();
			List<RestOrderLine> orderLines = new ArrayList<RestOrderLine>();
			for (String row : rows) {
				String[] fields = row.split(FIELD_SEPARATOR);
				if (row.startsWith(RestOrder.ROW_TYPE_HEADER)) {					
					restOrder.setOrderNumber(fields[1]);
					restOrder.setOriginalOrderNumber(fields[2]);
				} else if (row.startsWith(RestOrder.ROW_TYPE_LINE)) {
					RestOrderLine line = new RestOrderLine();
					line.setLineId(Integer.parseInt(fields[1]));
					line.setArticleId(fields[2]);
					line.setTotal(Integer.parseInt(fields[3]));
					orderLines.add(line);
				}
			}
			restOrder.setOrderLines(orderLines);
			return restOrder;
		} catch (Exception e) {
			throw new PickImportException(ERROR_WHEN_RECEIVNING_RESTORDER_FILE + fileName);
		}
    }

    
    private OrderPickingInfo getPickingInfo(List<String> rows, String fileName) throws PickImportException{
    	try {
			OrderPickingInfo pickingInfo = new OrderPickingInfo();
			List<PickedOrderLine> orderLines = new ArrayList<PickedOrderLine>();
			for (String row : rows) {
				String[] fields = row.split(FIELD_SEPARATOR);
				if (row.startsWith(ROW_TYPE_HEADER)) {					
					if (fields.length >= 3) {
						pickingInfo.setOrderNumber(fields[1]);
						pickingInfo.setStatus(Integer.parseInt(fields[2]));
					}
				} else if (row.startsWith(ROW_TYPE_LINE)) {
					PickedOrderLine line = new PickedOrderLine();
					try {
						line.setLineId(Integer.parseInt(fields[1]));
					} catch (Exception e) {
						line.setLineId(-1);
					}
					line.setArticleId(fields[2]);
					line.setAmount(Integer.parseInt(fields[3]) + line.getAmount());
					if (fields.length > 4 && !StringUtils.isEmpty(fields[4])) {
						line.getSerialNumbers().add(fields[4]);
					}
					orderLines.add(line);
				}
			}
			pickingInfo.setPickedLines(orderLines);
			return pickingInfo;
		} catch (Exception e) {
			throw new PickImportException(ERROR_WHEN_RECEIVNING_PICKING_FILE + fileName);
		}
    }
}
