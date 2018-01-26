package se.lanteam.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import se.lanteam.constants.CustomFieldConstants;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.RestrictionCodes;
import se.lanteam.constants.SLAConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.exceptions.ReceiveOrderException;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderCustomFieldRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PropertyService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
@Transactional
public class OrderImportService {

	private static final String GENERAL_FILE_ERROR = "Fel vid inläsning av fil. ";
	private static final String ERROR_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Ordernummer saknas i fil: ";
	private static final String ERROR_CUSTOMER_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Kundens ordernummer saknas i fil: ";
	private static final String ERROR_NO_ORDER_LINES = GENERAL_FILE_ERROR + "Inga orderrader att leveransrapportera (kundradnummer eller restriktionskod saknas) i fil: ";
	private static final String ERROR_ARTICLE_ID_MISSING = GENERAL_FILE_ERROR + "Artikel-ID saknas på orderrad i fil: ";
	private static final String ERROR_ROW_NUMBER_MISSING = GENERAL_FILE_ERROR + "Orderradnummer saknas på orderrad i fil: ";
	private static final String ERROR_TOTAL_MISSING = GENERAL_FILE_ERROR + "Antal saknas på orderrad i fil: ";
	private static final String ERROR_ORDER_ALREADY_RECEIVED = GENERAL_FILE_ERROR + "Order har redan tagits emot";
	
	private static final String PROPERTY_WEBSHOP_INTEGRATION_ACTIVATED = "webshop-integration-activated";
	
	private static final Logger LOG = LoggerFactory.getLogger(OrderImportService.class);

    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    private PropertyService propService;
    private CustomerGroupRepository customerGroupRepo;
    private OrderCustomFieldRepository orderCustomFieldRepo;
    
    public void addJointDeliveryInfo() {
    	List<OrderHeader> unJoinedOrders = orderRepo.findOrdersJointDeliveryUnjoined(StatusConstants.ORDER_STATUS_NEW);
    	for (OrderHeader order : unJoinedOrders) {
    		if (CustomFieldConstants.VALUE_SAMLEVERANS_MASTER.equalsIgnoreCase(order.getJointDelivery())) {
    			// Master order
				List<OrderHeader> childOrders = orderRepo.findOrdersByJointDelivery(order.getNetsetOrderNumber());
				if (childOrders.size() > 0) {
					StringBuffer orders = new StringBuffer();
					boolean first = true;
					for (OrderHeader childOrder : childOrders) {
						if (!first)
							orders.append(",");
						orders.append(childOrder.getOrderNumber());
						first = false;
					}
					order.setJointDeliveryOrders(orders.toString());
					order.setJointDeliveryText(String.format(CustomFieldConstants.TEXT_SAMLEVERANS_MASTER, orders.toString()));
					orderRepo.save(order);
				}
    		} else {
    			// Child order
    			List<OrderHeader> masterOrders = orderRepo.findOrdersByNetsetOrderNumber(order.getJointDelivery());
    			if (masterOrders.size() > 0) {
    				LOG.debug("child, master-order-id: " + masterOrders.get(0).getId());
    				OrderHeader masterOrder = orderRepo.findOne(masterOrders.get(0).getId());
    				String masterOrderNr = masterOrder.getOrderNumber();
					order.setJointDeliveryText(String.format(CustomFieldConstants.TEXT_SAMLEVERANS_CHILD, masterOrderNr));
					order.setJointDeliveryOrders(masterOrderNr);
					order.setExcludeFromList(true);
					for (OrderCustomField field : order.getOrderCustomFields()) {
						orderCustomFieldRepo.delete(field.getId());
					}
					order.setOrderCustomFields(null);
					List<OrderCustomField> updatedCustomFields = new ArrayList<OrderCustomField>();
					for (OrderCustomField customField : masterOrder.getOrderCustomFields()) {
						LOG.debug("child, customField " + customField.getId() + ", " + customField.getValue());
						String value = customField.getValue();
						if (customField.getCustomField().getIdentification() == CustomFieldConstants.CUSTOM_FIELD_JOINT_DELIVERY) {
							value = masterOrder.getNetsetOrderNumber();
						}
						updatedCustomFields.add(new OrderCustomField(customField.getCustomField(), order, value, customField.getCreationDate()));
					}					
					order.setOrderCustomFields(updatedCustomFields);
					order.setContact1Email(masterOrder.getContact1Email());
					order.setContact1Name(masterOrder.getContact1Name());
					order.setContact1Phone(masterOrder.getContact1Phone());
					orderRepo.save(order);
    			}
    		}    		
    	}    	
    }
    
	public void moveFiles() throws IOException {
	    String fileSourceFolder = propService.getString(PropertyConstants.FILE_INCOMING_FOLDER);	    
	    String fileDestFolder = propService.getString(PropertyConstants.FILE_PROCESSED_FOLDER);	    
	    String fileErrorFolder = propService.getString(PropertyConstants.FILE_ERROR_FOLDER);	    
        LOG.debug("Looking for files to move!");
		final File inputFolder = new File(fileSourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				LOG.debug("FILE : " + fileEntry.getName());
				Path source = Paths.get(fileSourceFolder + "/" + fileEntry.getName());
				Path target = Paths.get(fileDestFolder + "/" + fileEntry.getName());
				Path errorTarget = Paths.get(fileErrorFolder + "/" + fileEntry.getName());
				try {
					StringBuilder sb = new StringBuilder();
					List<String> rows = Files.readAllLines(source);
					for (String row : rows) {
						sb.append(row);
					}
					OrderHeader orderHeader = getOrderHeaderFromDB(sb.toString());
					orderHeader = getOrderHeaderFromJson(sb.toString(), orderHeader);
					if (validate(orderHeader, fileEntry.getName())) {
						OrderComment comment = new OrderComment();
						comment.setMessage("Tack för din beställning!");
						comment.setOrderLine("0");
						comment.setOrderHeader(orderHeader);
						orderHeader.setOrderComments(new HashSet<OrderComment>());
						orderHeader.getOrderComments().add(comment);
						orderHeader.setReceivingStatus();
						orderHeader = fillEmptyRestrictionCodes(orderHeader);
						orderRepo.save(orderHeader);
						checkThatOrderCreated(orderHeader.getOrderNumber());
						Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
					} else {
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					}
					
				} catch (ReceiveOrderException e) {
					saveError(e.getMessage());
					Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	private OrderHeader getOrderHeaderFromDB(String json) throws ReceiveOrderException {
		JSONObject jsonOrder = new JSONObject(json);
		String orderNumber = String.valueOf(jsonOrder.optInt("Ordernummer"));
		List<OrderHeader> orderHeaderList = orderRepo.findOrdersByOrderNumber(orderNumber);
		if (orderHeaderList.size() > 0) {
			throw new ReceiveOrderException(orderNumber, ERROR_ORDER_ALREADY_RECEIVED);
		} else {
			String netetOrderNumber = String.valueOf(jsonOrder.optInt("Netset_ordernummer"));
			orderHeaderList = orderRepo.findOrdersByNetsetOrderNumber(netetOrderNumber);
			if (orderHeaderList.size() > 0) {
				return orderRepo.findOne(orderHeaderList.get(0).getId());
			} else {
				return null;
			}
		}
	}
    
	private OrderHeader getOrderHeaderFromJson(String json, OrderHeader orderHeader) {
		if (orderHeader == null) {
			orderHeader = new OrderHeader();
		}
		JSONObject jsonOrder = new JSONObject(json);
		orderHeader.setOrderNumber(String.valueOf(jsonOrder.optInt("Ordernummer")));
		orderHeader.setNetsetOrderNumber(String.valueOf(jsonOrder.optInt("Netset_ordernummer")));
		orderHeader.setOrderDate(jsonDateToDate(jsonOrder.optString("Orderdatum")));
		orderHeader.setCustomerName(jsonOrder.optString("Kundnamn"));
		orderHeader.setCustomerNumber(jsonOrder.optString("Kundnummer"));
		orderHeader.setPostalAddress1(jsonOrder.optString("Postadress1"));
		orderHeader.setPostalAddress2(jsonOrder.optString("Postadress2"));
		orderHeader.setPostalCode(jsonOrder.optString("Postnummer"));
		orderHeader.setCity(jsonOrder.optString("Ort"));
		orderHeader.setDeliveryAddressName(jsonOrder.optString("Leveransadress_namn"));
		orderHeader.setDeliveryPostalAddress1(jsonOrder.optString("Leveransadress_postadress1"));
		orderHeader.setDeliveryPostalAddress2(jsonOrder.optString("Leveransadress_postadress2"));
		orderHeader.setDeliveryPostalCode(jsonOrder.optString("Leveransadress_postnummer"));
		orderHeader.setDeliveryCity(jsonOrder.optString("Leveransadress_ort"));
		orderHeader.setLeasingNumber(jsonOrder.optString("Leasingavtal"));		
		orderHeader.setCustomerOrderNumber(jsonOrder.optString("Intraservice_ordernummer"));
		orderHeader.setCustomerSalesOrder(jsonOrder.optString("Intraservice_beställningsnummer"));
		orderHeader.setPartnerId(jsonOrder.optString("PartnerId"));
		if (!orderHeader.getContactInfoFromNetset()) {
			orderHeader.setContact1Name(jsonOrder.optString("Kontakt1_namn"));
			orderHeader.setContact1Email(jsonOrder.optString("Kontakt1_epost"));
			orderHeader.setContact1Phone(jsonOrder.optString("Kontakt1_telefon"));
		}
		orderHeader.setContact2Name(jsonOrder.optString("Kontakt2_namn"));
		orderHeader.setContact2Email(jsonOrder.optString("Kontakt2_epost"));
		orderHeader.setContact2Phone(jsonOrder.optString("Kontakt2_telefon"));
		orderHeader.setStatus(StatusConstants.ORDER_STATUS_NEW);
		JSONArray jsonOrderLines = jsonOrder.getJSONArray("Orderrader");
		StringBuffer articleNumbers = new StringBuffer();
		for (int i = 0; i < jsonOrderLines.length(); i++) {			
			JSONObject jsonOrderLine = jsonOrderLines.getJSONObject(i);
			OrderLine orderLine = new OrderLine();
			orderLine.setRowNumber(jsonOrderLine.optInt("Radnummer"));
			orderLine.setCustomerRowNumber(jsonOrderLine.optInt("KundRadnummer", 0));
			orderLine.setArticleNumber(jsonOrderLine.optString("Artikelnummer"));
			orderLine.setArticleDescription(jsonOrderLine.optString("Artikelbenämning"));
			orderLine.setTotal(jsonOrderLine.optInt("Antal"));
			orderLine.setRemaining(jsonOrderLine.optInt("Antal"));
			orderLine.setRegistered(0);
			orderLine.setRestrictionCode(jsonOrderLine.optString("Restriktionskod"));
			orderLine.setOrganisationUnit(jsonOrderLine.optString("Instruktion_1"));
			orderLine.setInstallationType(jsonOrderLine.optString("Instruktion_2"));
			orderLine.setOperatingSystem(jsonOrderLine.optString("Instruktion_3"));
			orderLine.setOrderHeader(orderHeader);
			if (i == 0) {
				orderHeader.setOrderLines(new HashSet<OrderLine>());
			}
			orderHeader.getOrderLines().add(orderLine);
			articleNumbers.append(orderLine.getArticleNumber() + ";");
		}
		orderHeader.setArticleNumbers(articleNumbers.toString());
		orderHeader.setReceivedFromERP(true);
		// check if webshop integration is active
		Long wsIntegrationActivated = propService.getLong(PROPERTY_WEBSHOP_INTEGRATION_ACTIVATED);
		if (wsIntegrationActivated == 0) {
			// Not acticated
			orderHeader.setReceivedFromWebshop(true);
			String intraGroupName = propService.getString(PropertyConstants.CUSTOMER_GROUP_INTRASERVICE);
			orderHeader.setCustomerGroup(customerGroupRepo.findByName(intraGroupName));
			orderHeader.setStatus(StatusConstants.ORDER_STATUS_NEW);
		}
		orderHeader.setSlaDays(getSlaDays(orderHeader));
		return orderHeader;
	}
	
	private boolean validate(OrderHeader orderHeader, String fileName) {
		if (orderHeader.getOrderNumber().equals("0")) {
			saveError(ERROR_ORDER_NUMBER_MISSING + fileName);
			return false;
		}
		if (StringUtils.isEmpty(orderHeader.getCustomerOrderNumber())) {
			saveError(ERROR_CUSTOMER_ORDER_NUMBER_MISSING + fileName);
			return false;
		}
		if (orderHeader.getOrderLines().isEmpty()) {
			saveError(ERROR_NO_ORDER_LINES + fileName);
			return false;
		} else {
			boolean restrictionCodedLineExist = false;
			for (OrderLine line : orderHeader.getOrderLines()) {
				if (StringUtils.isEmpty(line.getArticleNumber())) {
					saveError(ERROR_ARTICLE_ID_MISSING + fileName);
					return false;					
				}
				if (StringUtils.isEmpty(line.getRowNumber())) {
					saveError(ERROR_ROW_NUMBER_MISSING + fileName);
					return false;					
				}
				if (StringUtils.isEmpty(line.getTotal())) {
					saveError(ERROR_TOTAL_MISSING + fileName);
					return false;					
				}
				if (!restrictionCodedLineExist) {
					restrictionCodedLineExist = StringUtils.hasText(line.getRestrictionCode());
				}
			}
			if (!restrictionCodedLineExist && !orderHeader.isChildOrderInJoint()) {
				saveError(ERROR_NO_ORDER_LINES + fileName);
				return false;
			}
		}
		return true;
	}

	private Integer getSlaDays(OrderHeader order) {
		Integer slaDays = SLAConstants.SLA_LONG;
		for (OrderLine ol : order.getOrderLines()) {
			if (ol.getCustomerRowNumber() > 0) {
				if ((ol.getRestrictionCode().equals(RestrictionCodes.SLA_NO_SERIALN0) || ol.getRestrictionCode().equals(RestrictionCodes.SLA_SERIALN0))
						&& ol.getTotal() <= SLAConstants.MAX_PER_ORDER_LINE) {
					slaDays = SLAConstants.SLA_SHORT;
				} else {
					return SLAConstants.SLA_LONG;
				}
			}
		}
		return slaDays;
	}
	private void checkThatOrderCreated(String orderNumber) {
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
		if (orders == null || orders.size() == 0) {
			saveError("Order " + orderNumber + " lästes in från Visma men ordern sparades ej i LIM.");
		}
	}
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
	private OrderHeader fillEmptyRestrictionCodes(OrderHeader oh) {
		for (OrderLine ol : oh.getOrderLines()) {
			if (StringUtils.isEmpty(ol.getRestrictionCode())) {
				ol.setRestrictionCode(RestrictionCodes.NO_SLA_NO_SERIALN0);
			}
		}
		return oh;
	}
	@Autowired
	public void setOrderCustomFieldRepo(OrderCustomFieldRepository orderCustomFieldRepo) {
		this.orderCustomFieldRepo = orderCustomFieldRepo;
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
	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}	
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerGroupRepo) {
		this.customerGroupRepo = customerGroupRepo;
	}

}
