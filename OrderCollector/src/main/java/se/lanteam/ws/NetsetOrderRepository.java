package se.lanteam.ws;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.SystemProperty;
import se.lanteam.repository.CustomFieldRepository;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderCustomFieldRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.repository.PropertyRepository;
import se.lanteam.ws.netset.CreateOrderRequest;
import se.lanteam.ws.netset.CreateOrderResponse;
import se.lanteam.ws.netset.InformationField;
import se.lanteam.ws.netset.ObjectFactory;

@Component
public class NetsetOrderRepository {

	private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    private CustomerGroupRepository customerGroupRepo;
    private CustomFieldRepository customFieldRepo;
    private OrderCustomFieldRepository orderCustomFieldRepo;
    private PropertyRepository propertyRepo;
	
    private static int RESULT_CODE_CREATED_OK = 0;
    private static int RESULT_CODE_UPDATED_OK = 1;
    private static int RESULT_CODE_ERROR_MANDATORY_DATA_MISSING = 2;
    private static int RESULT_CODE_ERROR_UNKNOWN_CUSTOMER_GROUP = 3;
    
    private static String DESCRIPTION_CREATED_OK = "Order skapad";
    private static String DESCRIPTION_UPDATED_OK = "Order uppdaterad";
    private static String DESCRIPTION_MANDATORY_DATA_MISSING = "Obligatorisk data saknas";
    private static String DESCRIPTION_UNKNOWN_CUSTOMER_GROUP = "Okänd kundgrupp";
    
    private static String ERROR_LOG_GENERAL_MESSAGE = "Fel vid mottagande av order från Netset: ";
    
    private static String MISSING = "Saknas";
    
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
    	int returnCode = RESULT_CODE_CREATED_OK;
    	String description = DESCRIPTION_CREATED_OK;
    	if (!validationOk(request)) {
    		saveError(ERROR_LOG_GENERAL_MESSAGE + DESCRIPTION_MANDATORY_DATA_MISSING + ". Ordernr: " + getOrderNoFromRequest(request, MISSING));
    		return getResponse(RESULT_CODE_ERROR_MANDATORY_DATA_MISSING, DESCRIPTION_MANDATORY_DATA_MISSING);
    	}
    	CustomerGroup customerGroup = customerGroupRepo.findOne(Long.parseLong(request.getOrderData().getValue().getHeader().getCustomerNumber()));    	
    	if (customerGroup == null) {
    		saveError(ERROR_LOG_GENERAL_MESSAGE + DESCRIPTION_UNKNOWN_CUSTOMER_GROUP + ". Customer group: " + getOrderNoFromRequest(request, MISSING));
    		return getResponse(RESULT_CODE_ERROR_UNKNOWN_CUSTOMER_GROUP, DESCRIPTION_UNKNOWN_CUSTOMER_GROUP);
    	}
    	
		OrderHeader order = null;
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber()));
		if (orders.isEmpty()) {
			order = new OrderHeader();
			order.setStatus(StatusConstants.ORDER_STATUS_NEW);
		} else {
			order = orders.get(0);
			returnCode = RESULT_CODE_UPDATED_OK;
			description = DESCRIPTION_UPDATED_OK;
			List<OrderCustomField> orderCustomFields = orderCustomFieldRepo.findByOrderId(order.getId());
			for (OrderCustomField orderCustField : orderCustomFields) {
				orderCustomFieldRepo.delete(orderCustField);
			}
		}
		
		order.setOrderNumber(String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber()));
		order.setCustomerGroup(customerGroup);
		order.setJointInvoicing(getJointInvoicing(request.getOrderData().getValue().getHeader().getCustomerNumber()));
		if (request.getOrderData().getValue().getInformationFields() != null 
				&& request.getOrderData().getValue().getInformationFields().getInformationField() != null) {
			Set<OrderCustomField> orderCustomFields = new HashSet<OrderCustomField>();
			for (InformationField infoField : request.getOrderData().getValue().getInformationFields().getInformationField()) {
				OrderCustomField orderCustomField = new OrderCustomField();
				orderCustomField.setCustomField(customFieldRepo.findOne(Long.valueOf(infoField.getIdentification())));
				orderCustomField.setValue(infoField.getData());
				orderCustomField.setOrderHeader(order);
				orderCustomFields.add(orderCustomField);
			}
			order.setOrderCustomFields(orderCustomFields);
		}

		orderRepo.save(order);
		return getResponse(returnCode, description);

    }
    
    private int getJointInvoicing (String customerNo) {
    	int result = 0;
    	SystemProperty jointInvProp = propertyRepo.getOne(PropertyConstants.JOINT_INVOICING_CUST_NUMBERS);
    	if (jointInvProp != null && !StringUtils.isEmpty(jointInvProp.getStringValue())) {
    		if (Arrays.asList(jointInvProp.getStringValue().split(";")).contains(customerNo)) {
    			result = 1;
    		}
    	}    	
    	return result;
    }
    
    private String getOrderNoFromRequest(CreateOrderRequest request, String defaultValue) {
    	String orderNumber = defaultValue;
    	if (request != null && request.getOrderData().getValue() != null && request.getOrderData().getValue().getHeader() != null 
    		&& request.getOrderData().getValue().getHeader().getOrderNumber() > 0) {
    		orderNumber = String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber());
    	}
    	return orderNumber;
    }

    private String getCustomerNoFromRequest(CreateOrderRequest request, String defaultValue) {
    	String custNumber = defaultValue;
    	if (request != null && request.getOrderData().getValue() != null && request.getOrderData().getValue().getHeader() != null 
    		&& !StringUtils.isEmpty(request.getOrderData().getValue().getHeader().getCustomerNumber())) {
    		custNumber = request.getOrderData().getValue().getHeader().getCustomerNumber();
    	}
    	return custNumber;
    }

    
    private boolean validationOk(CreateOrderRequest request) {
    	boolean result = false;
    	if (!StringUtils.isEmpty(getOrderNoFromRequest(request, "")) && !StringUtils.isEmpty(getCustomerNoFromRequest(request, ""))) {
    		result = true;
    	}
    	return result;
    }
    
    private CreateOrderResponse getResponse(int code, String desc) {
    	ObjectFactory factory = new ObjectFactory();
    	CreateOrderResponse response = factory.createCreateOrderResponse();
    	response.setReturnCode(code);
    	response.setDescription(desc);    	
		return response;
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
	public void setCustomerGroupRepo(CustomerGroupRepository customerGroupRepo) {
		this.customerGroupRepo = customerGroupRepo;
	}
	@Autowired
	public void setCustomFieldRepo(CustomFieldRepository customFieldRepo) {
		this.customFieldRepo = customFieldRepo;
	}
	@Autowired
	public void setOrderCustomFieldRepo(OrderCustomFieldRepository orderCustomFieldRepo) {
		this.orderCustomFieldRepo = orderCustomFieldRepo;
	}
	@Autowired
	public void setPropertyRepo(PropertyRepository propertyRepo) {
		this.propertyRepo = propertyRepo;
	}
}
