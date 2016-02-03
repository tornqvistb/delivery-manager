package se.lanteam;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.ws.gbgintraservice.ArrayOfGBCA003AErrorResponseLine;
import se.lanteam.ws.gbgintraservice.ErrorResponse;
import se.lanteam.ws.gbgintraservice.GBCA003AErrorResponse;
import se.lanteam.ws.gbgintraservice.GBCA003AErrorResponseLine;
import se.lanteam.ws.gbgintraservice.ObjectFactory;

@Component
public class ErrorsRepository {

	private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
	
	public JAXBElement<String> storeErrorReport(ErrorResponse request) {
		String resultCode = "OK";
		ObjectFactory factory = new ObjectFactory();
		GBCA003AErrorResponse errorResponse = request.getErrorResponse().getValue();
		String salesOrder = errorResponse.getYourSalesOrder();		
		List<OrderHeader> orders = orderRepo.findOrdersByCustomerSalesOrder(salesOrder);
		if (orders.size() > 0) {
			OrderHeader order = orders.get(0);
			StringBuilder sb = new StringBuilder();
			sb.append("<strong>Felrapport från Intraservice:</strong>");
			ArrayOfGBCA003AErrorResponseLine lineArray = errorResponse.getLine();
			if (lineArray != null && lineArray.getGBCA003AErrorResponseLine() != null && lineArray.getGBCA003AErrorResponseLine().size() > 0) {
				for (GBCA003AErrorResponseLine line : lineArray.getGBCA003AErrorResponseLine()) {
					sb.append("<br/>");
					if (!StringUtils.isEmpty(line.getLineId())) {
						sb.append(" Radnummer: " + line.getLineId());	
					}					
					if (!StringUtils.isEmpty(line.getInvNo())) {
						sb.append(" Inventarienummer: " + line.getInvNo());	
					}
					if (!StringUtils.isEmpty(line.getItemId())) {
						sb.append(" Itemnummer: " + line.getItemId());	
					}
					if (!StringUtils.isEmpty(line.getSerialNo())) {
						sb.append(" Serienummer: " + line.getSerialNo());	
					}
					if (!StringUtils.isEmpty(line.getStatus())) {
						sb.append(" Status: " + line.getStatus());	
					}
					if (!StringUtils.isEmpty(line.getErrorText())) {
						sb.append("<br/><strong>Felmeddelande: </strong>" + line.getErrorText());	
					}
				}
				order.setTransmitErrorMessage(sb.toString());
				order.setStatus(StatusConstants.ORDER_STATUS_NOT_ACCEPTED);
				orderRepo.save(order);
			} else {
				resultCode = "Inga rader";
			}
		} else {
			resultCode = "Hittade ej order " + salesOrder;
		}
		
		JAXBElement<String> result = factory.createErrorResponseResponseErrorResponseResult(resultCode);
		return result;
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
