package se.lanteam.service;

import static se.lanteam.model.OrderPickingInfo.FIELD_SEPARATOR;
import static se.lanteam.model.OrderPickingInfo.ROW_TYPE_HEADER;
import static se.lanteam.model.OrderPickingInfo.ROW_TYPE_LINE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.exceptions.PickImportException;
import se.lanteam.helpers.OrderCloneHelper;
import se.lanteam.model.OrderPickingInfo;
import se.lanteam.model.PickedOrderLine;
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

	@Transactional
    public void manageOrderPickFiles() throws IOException{
	    String fileSourceFolder = propService.getString(PropertyConstants.FILE_INCOMING_WH_FOLDER);	    
	    String fileDestFolder = propService.getString(PropertyConstants.FILE_PROCESSED_WH_FOLDER);	    
	    String fileErrorFolder = propService.getString(PropertyConstants.FILE_ERROR_WH_FOLDER);	            
		final File inputFolder = new File(fileSourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				logger.info("Found file: " + fileEntry.getName());
				Path source = Paths.get(fileSourceFolder + "/" + fileEntry.getName());
				Path target = Paths.get(fileDestFolder + "/" + fileEntry.getName());
				Path errorTarget = Paths.get(fileErrorFolder + "/" + fileEntry.getName());
				try {
					List<String> rows = Files.readAllLines(source);
					OrderPickingInfo pickingInfo = getPickingInfo(rows, fileEntry.getName());
					if (!StringUtils.isEmpty(pickingInfo.getOriginalOrderNumber())) {
						createRestOrder(pickingInfo);
					}
					updateOrder(pickingInfo);						
					logger.info(pickingInfo.toString());
					Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
				} catch (PickImportException e) {
					errorRepo.save(new ErrorRecord(e.getMessage()));
					Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
    }
    
    private void createRestOrder(OrderPickingInfo pickingInfo) {
    	List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(pickingInfo.getOriginalOrderNumber());
    	if (!orderList.isEmpty()) {
    		OrderHeader originalOrder = orderList.get(0);
    		OrderHeader restOrder = OrderCloneHelper.cloneOrder(originalOrder, pickingInfo);
    		orderRepo.save(restOrder);
    	}    	
    }	
	
    private void updateOrder(OrderPickingInfo pickingInfo) {
    	List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(pickingInfo.getOrderNumber());
    	if (!orderList.isEmpty()) {
    		OrderHeader order = orderList.get(0);
    		//Check status, maybe change this condition later!!
    		if (order.getEditable()) {
    			for (OrderLine line : order.getOrderLines()) {
    				for (PickedOrderLine pickedLine : pickingInfo.getPickedLines()) {
    					if (line.getRowNumber() == pickedLine.getLineId()) {
    						if (pickedLine.getSerialNumbers().isEmpty()) {
    							line.addPickedQuantity(pickedLine.getAmount());
    						} else {
    							line.addPickedSerialNumbers(pickedLine.getSerialNumbers());
    						}
    						break;
    					}
    				}
    			}
    		}
    		order.setPickStatus(pickingInfo.getStatus());
    		//TODO Change logic for setting order status later
    		order.setStatus(StatusConstants.ORDER_STATUS_STARTED);
    		orderRepo.save(order);
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
						if (fields.length >= 4) {
							pickingInfo.setOriginalOrderNumber(fields[3]);
						}
					}
				} else if (row.startsWith(ROW_TYPE_LINE)) {
					PickedOrderLine line = getLineById(orderLines, row);
					if (line == null) {
						line = new PickedOrderLine();
					}					
					line.setLineId(Integer.parseInt(fields[1]));
					line.setArticleId(fields[2]);
					if (fields.length > 4 && !StringUtils.isEmpty(fields[4])) {
						line.getSerialNumbers().add(fields[4]);
					} else {
						line.setAmount(Integer.parseInt(fields[3]));
					}
					orderLines = updateList(orderLines, line);
				}
			}
			pickingInfo.setPickedLines(orderLines);
			return pickingInfo;
		} catch (Exception e) {
			throw new PickImportException("Fel uppstod vid inläsning av fil från Lexit: " + fileName);
		}
    }

    private List<PickedOrderLine> updateList(List<PickedOrderLine> lines, PickedOrderLine line) {
    	for (PickedOrderLine currLine : lines) {
    		if (currLine.getLineId() == line.getLineId()) {
    			currLine = line;
    			return lines;
    		}
    	}
    	lines.add(line);
    	return lines;
    }
    
    private PickedOrderLine getLineById(List<PickedOrderLine> orderLines, String line) {
    	int lineId = Integer.parseInt(line.split(FIELD_SEPARATOR)[1]);
    	for (PickedOrderLine orderLine : orderLines) {
    		if (orderLine.getLineId() == lineId) {
    			return orderLine;
    		}
    	}
    	return null;
    	
    }
    
}
