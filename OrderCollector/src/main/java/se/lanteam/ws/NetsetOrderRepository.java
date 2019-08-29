package se.lanteam.ws;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import se.lanteam.constants.CustomFieldConstants;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.SystemProperty;
import se.lanteam.repository.CustomFieldRepository;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderCustomFieldRepository;
import se.lanteam.repository.OrderLineRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.repository.PropertyRepository;
import se.lanteam.service.CommonOrderService;
import se.lanteam.ws.netset.CreateOrderRequest;
import se.lanteam.ws.netset.CreateOrderResponse;
import se.lanteam.ws.netset.InformationField;
import se.lanteam.ws.netset.ObjectFactory;

@Component
public class NetsetOrderRepository {

	private OrderRepository orderRepo;
	private OrderLineRepository orderLineRepo;
    private ErrorRepository errorRepo;
    private CustomerGroupRepository customerGroupRepo;
    private CustomFieldRepository customFieldRepo;
    private OrderCustomFieldRepository orderCustomFieldRepo;
    private PropertyRepository propertyRepo;
    
    @Autowired
    private CommonOrderService commonOrderService;
	
    private static final int RESULT_CODE_CREATED_OK = 0;
    private static final int RESULT_CODE_UPDATED_OK = 1;
    private static final int RESULT_CODE_ERROR_MANDATORY_DATA_MISSING = 2;
    private static final int RESULT_CODE_ERROR_UNKNOWN_CUSTOMER_GROUP = 3;
    
    private static final String DESCRIPTION_CREATED_OK = "Order skapad";
    private static final String DESCRIPTION_UPDATED_OK = "Order uppdaterad";
    private static final String DESCRIPTION_MANDATORY_DATA_MISSING = "Obligatorisk data saknas";
    private static final String DESCRIPTION_UNKNOWN_CUSTOMER_GROUP = "Okänd kundgrupp";
    
    private static final String ERROR_LOG_GENERAL_MESSAGE = "Fel vid mottagande av order från Netset: ";
    
    private static final String MISSING = "Saknas";
    
    private static final String POSTFIX_NETSET_ORDER_NO = "-N";
    
    private static final Logger LOG = LoggerFactory.getLogger(NetsetOrderRepository.class);
        
    private static final String PROCESSED_FILES_DIR = "processed/";
    private static final String INCOMING_FILES_DIR = "incoming/";
    private static final String ERROR_FILES_DIR = "error/";
    
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
    	String baseDir = propertyRepo.findById(PropertyConstants.NETSET_FILES_BASEDIR).getStringValue();
    	logOrder(request);
    	saveOrderToXMLFile(request, baseDir + INCOMING_FILES_DIR);
    	int returnCode = RESULT_CODE_CREATED_OK;
    	String description = DESCRIPTION_CREATED_OK;
    	if (!validationOk(request)) {
    		saveError(ERROR_LOG_GENERAL_MESSAGE + DESCRIPTION_MANDATORY_DATA_MISSING + ". Ordernr: " + getOrderNoFromRequest(request, MISSING));
    		saveOrderToXMLFile(request, baseDir + ERROR_FILES_DIR);
    		return getResponse(RESULT_CODE_ERROR_MANDATORY_DATA_MISSING, DESCRIPTION_MANDATORY_DATA_MISSING);
    	}
    	CustomerGroup customerGroup = customerGroupRepo.findByName(getCustomerGroupFromRequest(request, MISSING));    	
    	if (customerGroup == null) {
    		LOG.info("Did not find customer group in DB: " + getCustomerGroupFromRequest(request, MISSING));
    		saveOrderToXMLFile(request, baseDir + ERROR_FILES_DIR);
    		return getResponse(RESULT_CODE_ERROR_UNKNOWN_CUSTOMER_GROUP, DESCRIPTION_UNKNOWN_CUSTOMER_GROUP);
    	}
    	
		OrderHeader order = null;
		List<OrderHeader> orders = orderRepo.findOrdersByNetsetOrderNumber(String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber()));
		if (orders.isEmpty()) {
			order = new OrderHeader();
			order.setOrderNumber(String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber()) + POSTFIX_NETSET_ORDER_NO);
		} else {
			order = orders.get(0);
			returnCode = RESULT_CODE_UPDATED_OK;
			description = DESCRIPTION_UPDATED_OK;
			List<OrderCustomField> orderCustomFields = orderCustomFieldRepo.findByOrderId(order.getId());
			for (OrderCustomField orderCustField : orderCustomFields) {
				orderCustomFieldRepo.delete(orderCustField);
			}
		}
				
		order.setNetsetOrderNumber(String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber()));
		order.setCustomerGroup(customerGroup);
		order.setJointInvoicing(getJointInvoicing(request.getOrderData().getValue().getHeader().getCustomerNumber()));
		if (request.getOrderData().getValue().getInformationFields() != null 
				&& request.getOrderData().getValue().getInformationFields().getInformationField() != null)	 {
			Set<OrderCustomField> orderCustomFields = new HashSet<OrderCustomField>();
			for (InformationField infoField : request.getOrderData().getValue().getInformationFields().getInformationField()) {
				OrderCustomField orderCustomField = new OrderCustomField();
				orderCustomField.setCustomField(customFieldRepo.findOne(Long.valueOf(infoField.getIdentification())));
				orderCustomField.setValue(infoField.getData());
				orderCustomField.setOrderHeader(order);
				orderCustomFields.add(orderCustomField);
				if (customerGroup.getGetContactInfoFromNetset()) {
					order = checkForMatchingField(order, infoField);
				}
				order = checkForJointDelivery(order, infoField);
			}
			order.setOrderCustomFields(orderCustomFields);
		}
		order.setContactInfoFromNetset(customerGroup.getGetContactInfoFromNetset());
		order.setReceivedFromWebshop(true);
		order.setReceivingStatus();
		if (order.getOrderDate() == null) {
			order.setOrderDate(new Date());
		}		
		order.setOrderLines(orderLineRepo.findByOrderId(order.getId())); 
		order = commonOrderService.doAutoRegistrationIfNeeded(order);
		orderRepo.save(order);
		saveOrderToXMLFile(request, baseDir + PROCESSED_FILES_DIR);
		return getResponse(returnCode, description);

    }
    
    private OrderHeader checkForMatchingField(OrderHeader order, InformationField infoField) {
    	if (infoField.getIdentification() == CustomFieldConstants.CUSTOM_FIELD_CONTACT_NAME) {
    		order.setContact1Name(infoField.getData());
    	} else if (infoField.getIdentification() == CustomFieldConstants.CUSTOM_FIELD_CONTACT_EMAIL) {
    		order.setContact1Email(infoField.getData());
    	} else if (infoField.getIdentification() == CustomFieldConstants.CUSTOM_FIELD_CONTACT_PHONE) {
        	order.setContact1Phone(infoField.getData());
    	}
    	return order;
    }

    private OrderHeader checkForJointDelivery(OrderHeader order, InformationField infoField) {
    	if (infoField.getIdentification() == CustomFieldConstants.CUSTOM_FIELD_JOINT_DELIVERY) {
    		order.setJointDelivery(infoField.getData());
    		if (CustomFieldConstants.VALUE_SAMLEVERANS_MASTER.equalsIgnoreCase(infoField.getData())) {
    			order.setJointDelivery(CustomFieldConstants.VALUE_SAMLEVERANS_MASTER);
    		}
    	}
    	return order;
    }

    
    private int getJointInvoicing (String customerNo) {
    	int result = 0;
    	SystemProperty jointInvProp = propertyRepo.findById(PropertyConstants.JOINT_INVOICING_CUST_NUMBERS);
    	if (jointInvProp != null && jointInvProp.getStringValue() != null) {
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

    private String getCustomerGroupFromRequest(CreateOrderRequest request, String defaultValue) {
    	String custGroup = defaultValue;
    	if (request != null && request.getOrderData().getValue() != null && request.getOrderData().getValue().getHeader() != null 
    		&& !StringUtils.isEmpty(request.getOrderData().getValue().getHeader().getCustomerGroupName())) {
    		custGroup = request.getOrderData().getValue().getHeader().getCustomerGroupName();
    	}
    	return custGroup;
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
	
	private void logOrder(CreateOrderRequest request) {
		try {
			LOG.info("New order from Netset:");
			LOG.info("-- customer group: " + getCustomerGroupFromRequest(request, MISSING));
			LOG.info("-- netset order number: " + String.valueOf(request.getOrderData().getValue().getHeader().getOrderNumber()));
			LOG.info(request.getOrderData().toString());
		} catch (Exception e) {
			LOG.info("Exception in logOrder");
		}
	}
	
	private void saveOrderToXMLFile(CreateOrderRequest request, String fileDir) {	    
	    try {
	        JAXBContext context = JAXBContext.newInstance(CreateOrderRequest.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
	        File file = new File( fileDir + getOrderNoFromRequest(request, "unknown") + ".xml" );
	        m.marshal( request, file );
	    } catch (JAXBException e) {
	        LOG.error("Failed to generate xml file for netset order " + getOrderNoFromRequest(request, "unknown"));
	    }
	}
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setOrderLineRepo(OrderLineRepository orderLineRepo) {
		this.orderLineRepo = orderLineRepo;
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
