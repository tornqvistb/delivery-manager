package se.lanteam.ws;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.lanteam.domain.ErrorRecord;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.ws.netset.CreateOrderRequest;
import se.lanteam.ws.netset.InformationField;
import se.lanteam.ws.netset.ObjectFactory;

@Component
public class NetsetOrderRepository {

	private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
	
    public JAXBElement<String> createOrder(CreateOrderRequest request) {
    	String resultCode = "OK";
    	
    	if (request != null) {
    		System.out.println("id:" + request.getOrderData().getValue().getId());
    		System.out.println("order number:" + request.getOrderData().getValue().getHeader().getOrderNumber());
    		System.out.println("cust number:" + request.getOrderData().getValue().getHeader().getCustomerNumber());
    		System.out.println("cust group name:" + request.getOrderData().getValue().getHeader().getCustomerGroupName());
    		
    		for (InformationField infoField : request.getOrderData().getValue().getInformationFields().getInformationField()) {
    			System.out.println("info id:" + infoField.getIdentification());
    			System.out.println("info label:" + infoField.getLabel());
    			System.out.println("info data:" + infoField.getData());
    		}
    	}
    	
    	ObjectFactory factory = new ObjectFactory();
    	JAXBElement<String> result = factory.createCreateOrderResponseOrderResponseData(resultCode);
		return result;

    }
    /*
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
				saveError("Fel vid hantering av felrapport från Intraservice: det finns inga orderrader i meddelandet");
			}
		} else {
			resultCode = "Hittade ej order " + salesOrder;
			saveError("Fel vid hantering av felrapport från Intraservice: Hittade ej order " + salesOrder);
		}
		
		JAXBElement<String> result = factory.createErrorResponseResponseErrorResponseResult(resultCode);
		return result;
	}
	*/
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
