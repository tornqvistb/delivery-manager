package se.lanteam.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import se.lanteam.constants.CustomFieldConstants;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.RestrictionCodes;
import se.lanteam.constants.SLAConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.SystemProperty;
import se.lanteam.exceptions.ReceiveOrderException;
import se.lanteam.repository.CustomFieldRepository;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.repository.PropertyRepository;
import se.lanteam.services.PropertyService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class ShopOrderImportService {

		
	private static final Logger LOG = LoggerFactory.getLogger(ShopOrderImportService.class);

	@Autowired
    private OrderRepository orderRepo;
	@Autowired
    private ErrorRepository errorRepo;
	@Autowired
    private PropertyService propService;
    @Autowired
    private CustomerGroupRepository customerGroupRepo;
    @Autowired
    private CustomFieldRepository customFieldRepo;
        
    private static final String ERROR_LOG_GENERAL_MESSAGE = "Fel vid mottagande av order från Netset, ";
    private static final String ERROR_UNKNOWN_CUSTOMER_GROUP = "Okänd kundgrupp: ";
    
	private static final String GENERAL_FILE_ERROR = "Fel vid inläsning av fil från Netset. ";
	private static final String ERROR_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Ordernummer saknas i fil: ";
	private static final String ERROR_CUSTOMER_ORDER_NUMBER_MISSING = GENERAL_FILE_ERROR + "Kundens ordernummer saknas i fil: ";
	private static final String ERROR_NO_ORDER_LINES = GENERAL_FILE_ERROR + "Inga orderrader att leveransrapportera (kundradnummer eller restriktionskod saknas) i fil: ";
	private static final String ERROR_COULD_NOT_PARSE_FILE = GENERAL_FILE_ERROR + "Filnamn: ";
	private static final String FILE_ENDING_SHOP = ".xml";
	private static final String SHOP_STATUS_CANCEL_ORDER = "600";
	private static final String EXTRINSIC_FIELD_RESTRICTION_CODE = "VLCdata";
	private static final String THANK_YOU_MESSAGE_COMMENT = "Tack för din beställning!";
	
    public void importFiles() throws IOException  {
	    String fileSourceFolder = propService.getString(PropertyConstants.FILE_INCOMING_SHOP_FOLDER);	    
	    String fileDestFolder = propService.getString(PropertyConstants.FILE_PROCESSED_SHOP_FOLDER);	    
	    String fileErrorFolder = propService.getString(PropertyConstants.FILE_ERROR_SHOP_FOLDER);	            
		final File inputFolder = new File(fileSourceFolder);
		LOG.info("Folder for Shop files: " + fileSourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				LOG.info("Found file: " + fileEntry.getName());
				if (fileNamePatternMatches(fileEntry.getName())) {
					//Process and create order
					Path source = Paths.get(fileSourceFolder + "/" + fileEntry.getName());
					Path target = Paths.get(fileDestFolder + "/" + fileEntry.getName());
					Path errorTarget = Paths.get(fileErrorFolder + "/" + fileEntry.getName());
					try {
						parseFile(fileEntry);
						Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
					} catch (SAXException e) {
						LOG.error("SAXException", e);
						saveError(ERROR_COULD_NOT_PARSE_FILE + fileEntry.getName());						
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					} catch (ParserConfigurationException e) {
						LOG.error("ParserConfigurationException", e);
						saveError(ERROR_COULD_NOT_PARSE_FILE + fileEntry.getName());
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					} catch (ReceiveOrderException e) {
						LOG.error("ReceiveOrderException", e);
						if (e.isLoggErrorInDb()) {
							saveError(e.getMessage());
						}
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						LOG.error("Exception", e);
						saveError(ERROR_COULD_NOT_PARSE_FILE + fileEntry.getName());
						Files.move(source, errorTarget, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}
    }
    
    private boolean fileNamePatternMatches(String fileName) {
    	return fileName != null && fileName.endsWith(FILE_ENDING_SHOP);
    }
    
    private void parseFile(File file) throws SAXException, IOException, ParserConfigurationException, ReceiveOrderException {
    	OrderHeader orderHeader = new OrderHeader();
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        LOG.info("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("OrderHeader");
        Node node = null;
        if (nList.getLength() > 0) {
        	node = nList.item(0);
        	Element e = (Element) node;
        	String orderNumber = getTagValue(e, "OrderNumber");
        	if (orderExists(orderNumber)) {
        		LOG.debug("Incoming order already exists: " + orderNumber);
        		cancelOrderIfNeeded(orderNumber, getTagValue(e, "Status"));
        		return;
        	}
        	orderHeader.setCustomerGroup(getCustomerGroup(getTagValue(e,"CustomerGroupName"), orderNumber));
        	orderHeader.setOrderNumber(orderNumber);
    		orderHeader.setNetsetOrderNumber(orderNumber);
    		orderHeader.setOrderDate(rawDateToDate(getTagValue(e,"OrderDate", "raw")));
    		orderHeader.setCustomerName(getTagValue(e,"CompanyInformation/Name")); 
    		orderHeader.setCustomerNumber(getTagValue(e,"CustomerNo"));
    		orderHeader.setPostalAddress1(getTagValue(e,"AddressingInformation/BillToAddress/Address1"));
    		orderHeader.setPostalAddress2(getTagValue(e,"AddressingInformation/BillToAddress/Address2"));
    		orderHeader.setPostalCode(getTagValue(e,"AddressingInformation/BillToAddress/Zip"));
    		orderHeader.setCity(getTagValue(e,"AddressingInformation/BillToAddress/City"));
    		orderHeader.setDeliveryAddressName(getTagValue(e,"AddressingInformation/ShipToAddress/AddressName"));
    		orderHeader.setDeliveryPostalAddress1(getTagValue(e,"AddressingInformation/ShipToAddress/Address1"));
    		orderHeader.setDeliveryPostalAddress2(getTagValue(e,"AddressingInformation/ShipToAddress/Address2"));
    		orderHeader.setDeliveryPostalCode(getTagValue(e,"AddressingInformation/ShipToAddress/Zip"));
    		orderHeader.setDeliveryCity(getTagValue(e,"AddressingInformation/ShipToAddress/City"));
    		orderHeader.setLeasingNumber(getTagValue(e,"InvoiceReference"));
    		orderHeader.setCustomerOrderNumber(getTagValue(e,"CustomerPO"));
    		orderHeader.setCustomerSalesOrder(getTagValue(e,"ExternalOrderNumber"));
    		orderHeader.setPartnerId("");                                                                            // Alltid tomt i tidigare Visma-filer
			orderHeader.setContact1Name(getTagValue(e,"ContactInformation/Contact"));
			orderHeader.setContact1Email(getTagValue(e,"ContactInformation/Email"));
			orderHeader.setContact1Phone(getTagValue(e,"ContactInformation/Phone"));
    		orderHeader.setContact2Name(getTagValue(e,"InvoiceContactInformation/Name"));
    		orderHeader.setContact2Email(getTagValue(e,"InvoiceContactInformation/Email"));
    		orderHeader.setContact2Phone(getTagValue(e,"InvoiceContactInformation/Phone"));
    		
    		NodeList orderLineNodes = doc.getElementsByTagName("OrderLines");
    		List<String> articleNumbers = new ArrayList<>();
    		Set<OrderLine> orderLines = new HashSet<>();
    		if (orderLineNodes.getLength() > 0) {
    			Element orderLinesEl = (Element) orderLineNodes.item(0);
    			NodeList productLines = orderLinesEl.getElementsByTagName("ProductLine");
    			for (int i = 0; i < productLines.getLength(); i++) {    				
    				Element lineEl = (Element) productLines.item(i);
    				OrderLine orderLine = new OrderLine();
    				orderLine.setArticleNumber(getTagValue(lineEl,"ManufacturerArticleNo"));    
    				orderLine.setArticleDescription(getTagValue(lineEl,"Label"));
    				orderLine.setRowNumber(Integer.valueOf(getTagValue(lineEl,"LineId")));
    				orderLine.setCustomerRowNumber(Integer.valueOf(getTagValue(lineEl,"LineId")));
    				int quantity = new Double(getTagValue(lineEl,"Quantity")).intValue();
    				orderLine.setTotal(quantity);
    				orderLine.setRemaining(quantity);
    				orderLine.setRegistered(0);
    				getInstallationAndFinancialInfo(orderLine, getTagValue(lineEl,"Comment"));
    				orderLine.setRestrictionCode(getRestrictionCode(lineEl));
    				orderLine.setOrderHeader(orderHeader);
    				orderLines.add(orderLine);
    				articleNumbers.add(orderLine.getArticleNumber());
    				
    			}
    		}
    		orderHeader.setOrderLines(orderLines);
    		addCustomFields(orderHeader, doc);
    		orderHeader.setSlaDays(getSlaDays(orderHeader));
    		orderHeader.setArticleNumbers(formatArticleNumbers(articleNumbers));
    		orderHeader.setReceivedFromERP(true);
    		orderHeader.setReceivedFromWebshop(true);
    		if (anyRestrictionCode(orderHeader.getOrderLines())) {
    			orderHeader.setStatus(StatusConstants.ORDER_STATUS_NOT_PICKED);
    		} else {
    			orderHeader.setStatus(StatusConstants.ORDER_STATUS_NOT_HANDLED);
    		}

    		addOrderComment(orderHeader);
    		if (validate(orderHeader, file.getName())) {
    			orderRepo.save(orderHeader);
    		}
            LOG.debug("Netset order: " + orderHeader.toString());        	
        } else {
        	throw new ReceiveOrderException("0", "No order in file", true);
        }
    }
    
    private String getRestrictionCode(Element lineElement)  {
		
		NodeList extrinsicFields = lineElement.getElementsByTagName("ExtrinsicFields");
		if (extrinsicFields.getLength() > 0) {
			Element extFieldsEl = (Element) extrinsicFields.item(0);
			NodeList fields = extFieldsEl.getElementsByTagName("Extrinsic");
			for (int i = 0; i < fields.getLength(); i++) {    				
				Element field = (Element) fields.item(i);
				String name = field.getAttribute("name");
				if (name != null && EXTRINSIC_FIELD_RESTRICTION_CODE.equals(name)) {
					return field.getTextContent();
				}
			}
		}
		return "";
	}
    
    private void cancelOrderIfNeeded(String orderNumber, String status) {
    	if (SHOP_STATUS_CANCEL_ORDER.equals(status)) {
    		List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(orderNumber);    		
    		OrderHeader order = orderList.get(0);
    		orderRepo.delete(order.getId());
    		LOG.debug("Netset order: " + orderNumber + " cancelled with status " + status);  
    	}
    }
    
    private boolean orderExists(String orderNumber) {
    	List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(orderNumber);
    	return orderList != null && !orderList.isEmpty(); 
    }
    
    private String getTagValue(Element e, String path) {
    	return getTagValue(e, path, null);
    }
    
    private String getTagValue(Element e, String path, String attribute) {
    	String[] elArr = path.split("/");
    	Element el;
    	try {
    		el = (Element) e.getElementsByTagName(elArr[0]).item(0);
    		if (elArr.length > 1 && !StringUtils.isEmpty(elArr[1])) {
    			el = (Element) el.getElementsByTagName(elArr[1]).item(0);
    		}
    		if (elArr.length > 2 && !StringUtils.isEmpty(elArr[2])) {
    			el = (Element) el.getElementsByTagName(elArr[2]).item(0);
    		}    		
    		if (el != null) {
    			if (attribute == null) {
    				return el.getTextContent();
    			} else {
    				return el.getAttribute(attribute);
    			}
    		} else {
    			return "";
    		}
		} catch (DOMException e1) {
			return "";
		}
    	
    }

    private Date rawDateToDate(String rawDate)
	{
	    long l = Long.parseLong(rawDate);
	    return new Date(l);
	}
    
    private CustomerGroup getCustomerGroup(String customerGroupName, String orderNumber) throws ReceiveOrderException {
        CustomerGroup customerGroup = customerGroupRepo.findByName(customerGroupName);    	
    	if (customerGroup != null) {
    		return customerGroup;
    	} else {
    		throw new ReceiveOrderException(orderNumber, ERROR_LOG_GENERAL_MESSAGE + ERROR_UNKNOWN_CUSTOMER_GROUP + customerGroupName, false);
    	}
    }
    
    private OrderLine getInstallationAndFinancialInfo(OrderLine line, String comment) {
    	if (!StringUtils.isEmpty(comment)) {
    		String[] installationArr = comment.split(";");
    		if (installationArr.length >= 3) {
    			line.setOrganisationUnit(installationArr[0]);
    			line.setInstallationType(installationArr[1]);
    			line.setOperatingSystem(installationArr[2]);
    		}
    		if (installationArr.length > 3) {
    			String[] financeInfoArr = installationArr[3].split("/");
    			if (financeInfoArr.length > 0) {
    				line.setLeasingNumber(financeInfoArr[0]);    				
    			}
    			if (financeInfoArr.length > 1) {
    				line.setRequestItemNumber(financeInfoArr[1]);    				
    			}
    		}
    	}
    	return line;
    }
	
    private Integer getSlaDays(OrderHeader order) {
		Integer slaDays = SLAConstants.SLA_LONG;
		for (OrderLine ol : order.getOrderLines()) {
			if (ol.getCustomerRowNumber() > 0 && ol.getRestrictionCode() != null) {
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
    	
	private String formatArticleNumbers(List<String> list) {
		ArrayList<String> newList = new ArrayList<>(); 
        for (String element : list) { 
            if (!newList.contains(element)) {   
                newList.add(element); 
            } 
        } 
		StringBuilder builder = new StringBuilder();
        for (String element : newList) { 
        	builder.append(element + ";");
        } 
        return builder.toString(); 
    }
	
	private void addCustomFields(OrderHeader orderHeader, Document doc)  {
		
		NodeList customFieldData = doc.getElementsByTagName("CustomFieldData");
		Set<OrderCustomField> orderCustomFields = new HashSet<OrderCustomField>();
		if (customFieldData.getLength() > 0) {
			Element custDataEl = (Element) customFieldData.item(0);
			NodeList fields = custDataEl.getElementsByTagName("Field");
			for (int i = 0; i < fields.getLength(); i++) {
				Element field = (Element) fields.item(i);
				OrderCustomField orderCustomField = new OrderCustomField();
				orderCustomField.setCustomField(customFieldRepo.findOne(Long.valueOf(field.getAttribute("id"))));
				orderCustomField.setValue(getTagValue(field, "Value"));
				if (!StringUtils.isEmpty(orderCustomField.getValue())){
					orderCustomField.setOrderHeader(orderHeader);
					orderCustomFields.add(orderCustomField);
				}
				if (orderHeader.getCustomerGroup().getGetContactInfoFromNetset()) {
					checkForMatchingField(orderHeader, orderCustomField);
				}
				checkForJointDelivery(orderHeader, orderCustomField);
			}
			orderHeader.setOrderCustomFields(orderCustomFields);
		}
	}

	private void addOrderComment(OrderHeader orderHeader)  {
		OrderComment comment = new OrderComment();
		comment.setMessage(THANK_YOU_MESSAGE_COMMENT);
		comment.setOrderLine("0");
		comment.setOrderHeader(orderHeader);
		orderHeader.setOrderComments(new HashSet<OrderComment>());
		orderHeader.getOrderComments().add(comment);
	}

	 private void checkForMatchingField(OrderHeader order, OrderCustomField customField) {
    	if (customField.getCustomField().getIdentification() == CustomFieldConstants.CUSTOM_FIELD_CONTACT_NAME) {
    		order.setContact1Name(customField.getValue());
    	} else if (customField.getCustomField().getIdentification() == CustomFieldConstants.CUSTOM_FIELD_CONTACT_EMAIL) {
    		order.setContact1Email(customField.getValue());
    	} else if (customField.getCustomField().getIdentification() == CustomFieldConstants.CUSTOM_FIELD_CONTACT_PHONE) {
        	order.setContact1Phone(customField.getValue());
    	}
    }

    private void checkForJointDelivery(OrderHeader order, OrderCustomField customField) {
    	if (customField.getCustomField().getIdentification() == CustomFieldConstants.CUSTOM_FIELD_JOINT_DELIVERY) {
    		order.setJointDelivery(customField.getValue());
    		if (CustomFieldConstants.VALUE_SAMLEVERANS_MASTER.equalsIgnoreCase(customField.getValue())) {
    			order.setJointDelivery(CustomFieldConstants.VALUE_SAMLEVERANS_MASTER);
    		}
    	}
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
		} 
		/* Vi skall acceptera alla ordrar, även dem som saknar restriktionskod
		if (!anyRestrictionCode(orderHeader.getOrderLines())) {
			saveError(ERROR_NO_ORDER_LINES + fileName);
			return false;			
		}
		*/
		return true;
	}
	
	private boolean anyRestrictionCode(Set<OrderLine> orderLines) {
		for (OrderLine ol: orderLines) {
			if (!isEmpty(ol.getRestrictionCode())) {
				return true;
			}
		}
		return false;
	}
	
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}

}
