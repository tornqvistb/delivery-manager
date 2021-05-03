package se.lanteam.helpers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.model.RestOrder;
import se.lanteam.model.RestOrderLine;

public class OrderCloneHelper {
	
	public static OrderHeader cloneOrder(OrderHeader originalOrder, RestOrder restOrder) {
		
		OrderHeader clone = originalOrder.cloneToRestOrder();
		
		clone.setOrderNumber(restOrder.getOrderNumber());
		
		Set<OrderLine> orderLines = new HashSet<OrderLine>();
		
		for (RestOrderLine restLine : restOrder.getOrderLines()) {			
			OrderLine orderLine = getLineFromOriginal(originalOrder, restLine.getArticleId());
			if (orderLine != null) {
				OrderLine newLine = orderLine.cloneToRestOrderLine();
				newLine.setTotal(restLine.getTotal());
				newLine.setRemaining(restLine.getTotal());
				newLine.setCustomerRowNumber(restLine.getLineId());
				newLine.setRowNumber(restLine.getLineId());
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
			newField.setOrderHeader(clone);
			customFields.add(newField);
		}
		
		clone.setOrderCustomFields(customFields);
		
		return clone;
	}
	
	private static OrderLine getLineFromOriginal(OrderHeader originalOrder, String articleId) {
		for (OrderLine line : originalOrder.getOrderLines()) {
			if (line.getArticleNumber().equals(articleId)) {
				return line;
			}
		}
		return null;
	}
		
}
