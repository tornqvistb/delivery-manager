package se.lanteam.helpers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.model.OrderPickingInfo;
import se.lanteam.model.PickedOrderLine;

public class OrderCloneHelper {
	
	public static OrderHeader cloneOrder(OrderHeader originalOrder, OrderPickingInfo pickingInfo) {
		
		OrderHeader clone = originalOrder.clone();
		
		clone.setOrderNumber(pickingInfo.getOrderNumber());
		
		clone.setStatus(StatusConstants.ORDER_STATUS_NEW);
		clone.setPickStatus(StatusConstants.PICK_STATUS_NOT_PICKED);
		
		Set<OrderLine> orderLines = new HashSet<OrderLine>();
		for (OrderLine line : originalOrder.getOrderLines()) {
			if (lineInPickingInfo(line, pickingInfo)) {
				OrderLine newLine = line.clone();
				newLine.setOrderHeader(clone);
				orderLines.add(newLine);
			}
		}
		
		clone.setOrderLines(orderLines);
		
		Set<OrderCustomField> customFields = new HashSet<OrderCustomField>();
		for (OrderCustomField customField : originalOrder.getOrderCustomFields()) {
			OrderCustomField newField = new OrderCustomField();
			newField.setCreationDate(new Date());
			newField.setCustomField(customField.getCustomField());
			newField.setOrderHeader(clone);
			newField.setValue(customField.getValue());
			customFields.add(newField);
		}
		
		clone.setOrderCustomFields(customFields);
		
		return clone;
	}
	
	private static boolean lineInPickingInfo(OrderLine line, OrderPickingInfo pickingInfo) {
		
		for (PickedOrderLine pickedLine : pickingInfo.getPickedLines()) {
			if (line.getRowNumber() == pickedLine.getLineId()) {
				return true;
			}
		}
		return false;
	}
	
}
