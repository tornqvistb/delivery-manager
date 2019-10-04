package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.FileConstants;
import se.lanteam.constants.LimStringUtil;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.Email;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.domain.ReportsConfig;
import se.lanteam.exceptions.MissingInputException;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.AttachmentRepository;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.PropertyRepository;
import se.lanteam.services.ExcelGenerator;
import se.lanteam.services.ExcelViewBuilder;

@Controller
public class DeliveryReportController extends BaseController{
	
	private ExcelGenerator excelGenerator;
	private AttachmentRepository attachmentRepo;
	private EmailRepository emailRepo;
	private PropertyRepository propertyRepo;
	
	private static final String EXCEL_EXPORT_FILE_NAME = "Leveransrapport-#customer-" + DateUtil.dateToString(new Date()) + FileConstants.FILE_ENDING_EXCEL;
	private static final String MAIL_SUBJECT = "Leveransrapport från Visolit";
	private static final Logger LOG = LoggerFactory.getLogger(DeliveryReportController.class);
	
	@RequestMapping("reports/delivery")
	public String showDeliveryReport(ModelMap model) {
		model.put("customerGroups", customerRepo.findAll());
		RequestAttributes reqAttr = new RequestAttributes();
		model.put("reqAttr", reqAttr);
		return "delivery-report";
	}

	@RequestMapping(value = "reports/delivery/changecustomer/{customerId}", method = RequestMethod.GET)
	public String changeCustomer(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long customerId,
			ModelMap model) {
		reqAttr.setCustomerCustomFields(getCustomerCustomFields(customerId));
		reqAttr.setCustomerId(customerId);		
		model.put("reqAttr", reqAttr);
		model.put("customerGroups", customerRepo.findAll());
		return "delivery-report";
	}
	
	@RequestMapping(value="reports/delivery/search", method=RequestMethod.GET)
	public String searchOrdersDelivery(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {

		try {
			if (reqAttr.getCustomerId() == 0) {
				throw new MissingInputException("Du måste välja kundgrupp");
			}
			
			Date fromDate = DateUtil.stringToDate(LimStringUtil.NVL(reqAttr.getFromDate(), DateUtil.getDefaultStartDateAsString()));
			Date toDate = DateUtil.stringToDateMidnight(LimStringUtil.NVL(reqAttr.getToDate(), DateUtil.getTodayAsString()));
			String fromOrderNo = LimStringUtil.NVL(reqAttr.getFromOrderNo(), LimStringUtil.firstOrderNo);
			String toOrderNo = LimStringUtil.NVL(reqAttr.getToOrderNo(), LimStringUtil.lastOrderNo);
			
			List<OrderHeader> orders;
			if (reqAttr.getCustomerId() != reqAttr.getZeroValue()) {
				orders = orderRepo.findDeliveredOrdersByCustGroup(fromDate, toDate, fromOrderNo, toOrderNo, reqAttr.getCustomerId());
			} else {
				orders = orderRepo.findDeliveredOrders(fromDate, toDate, fromOrderNo, toOrderNo);
			}
			reqAttr.setCustomerCustomFields(getCheckedCustomerCustomFields(reqAttr));
			if (!orders.isEmpty()) {
				reqAttr.setResultNotEmptyMsg("Sökningen gav " + orders.size() + " träff(ar)");
			} else {
				reqAttr.setResultEmptyMsg("Sökningen gav inga träffar");
			}
			model.put("reqAttr", reqAttr);
			model.put("orders", orders);
			model.put("customerGroups", customerRepo.findAll());
			searchBean.populate(orders, reqAttr.getCustomerId(), fromDate, toDate, fromOrderNo, toOrderNo, reqAttr.getCustomerCustomFields());
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
			model.put("reqAttr", reqAttr);
		} catch (MissingInputException e) {
			model.put("customerGroups", customerRepo.findAll());
			reqAttr = new RequestAttributes();
			reqAttr.setErrorMessage(e.getMessage());
			model.put("reqAttr", reqAttr);
		}
		return "delivery-report";
	}

	@RequestMapping(value="reports/delivery/export", method=RequestMethod.GET)
	public ModelAndView exportDeliveryToExcel(ModelMap model, HttpServletResponse response, @ModelAttribute RequestAttributes reqAttr) throws ParseException {
		model = getExcelDataIntoModel(model, reqAttr);
        response.setContentType( "application/ms-excel" );
        String fileName = EXCEL_EXPORT_FILE_NAME.replace("#customer", getCustomerNameFromsession());
        response.setHeader( "Content-disposition", "attachment; filename=" + fileName);         
		
		return new ModelAndView(new ExcelViewBuilder(), model);
	}

	@RequestMapping(value="reports/delivery/informCustomer", method=RequestMethod.GET)
	public String informCustomer(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {			
		model = getExcelDataIntoModel(model, reqAttr);
		// Create excel
		Workbook wb = excelGenerator.generate(model);
		if (wb != null) {
			byte[] content = excelGenerator.wbToByteArray(wb);
			if (content != null) {
				Attachment attachment = new Attachment();
				attachment.setFileContent(content);
				attachment.setFileName(EXCEL_EXPORT_FILE_NAME.replace("#customer", getCustomerNameFromsession()));
				attachment.setFileSize(Long.valueOf(content.length));
				attachment.setContentType("application/ms-excel");
				attachment = attachmentRepo.save(attachment);
				Email email = new Email();
				email.setAttachmentRef(attachment.getId());				
				email.setSubject(MAIL_SUBJECT);
				StringBuffer sb = new StringBuffer();
				sb.append("Hej!\n\n");
				sb.append("Bifogat finner ni en rapport med utförda leveranser från Visloit till " + getCustomerNameFromsession() + ".\n\n");
				sb.append("Med vänlig hälsning\n");
				sb.append("Visolit");								
				email.setContent(sb.toString());
				email.setSender(propertyRepo.findById(PropertyConstants.MAIL_USERNAME).getStringValue());
				email.setReceiver(getCustomerEmailFromsession());
				email.setReplyTo(propertyRepo.findById(PropertyConstants.MAIL_REPLY_TO_ADDRESS).getStringValue());
				email.setStatus(StatusConstants.EMAIL_STATUS_NEW);
				emailRepo.save(email);
				reqAttr.setCustomerCustomFields(searchBean.getCustomerCustomFields());
				reqAttr.setThanksMessage("Epost-meddelande skickat till kund");
			} else {
				reqAttr.setErrorMessage("Epost-meddelande kunde ej skapas");
			}
		} else {
			reqAttr.setErrorMessage("Epost-meddelande kunde ej skapas");
		}
		model.put("customerGroups", customerRepo.findAll());
		
		model.put("reqAttr", reqAttr);
		
		return "delivery-report";
	}

	private ModelMap getExcelDataIntoModel(ModelMap model, RequestAttributes reqAttr) {
		List<OrderHeader> orders = searchBean.getOrderList();
		
        //Sheet Name
        model.put("sheetname", "Leveransrapport");
        //Headers List
        List<String> headers = new ArrayList<String>();

        ReportsConfig reportsConfig = getReportsConfig();
        if (reportsConfig.getShowOrderNumber()) {
        	headers.add("Ordernummer");
        }
        if (reportsConfig.getShowNetsetOrderNumber()) {
        	headers.add("Web-ordernummer");
        }
        if (reportsConfig.getShowCustomerOrderNumber()) {
        	headers.add("Kundens ordernummer");
        }
        if (reportsConfig.getShowCustomerSalesOrder()) {
        	headers.add("Kundens leveransnummer");
        }
        if (reportsConfig.getShowLeasingNumber()) {
        	headers.add("Leasingnummer order");
        }
        if (reportsConfig.getShowOrderDate()) {
        	headers.add("Orderdatum");
        }
        if (reportsConfig.getShowDeliveryDate()) {
        	headers.add("Leveransdatum");
        }
        if (reportsConfig.getShowCustomerName()) {
        	headers.add("Kundens namn");
        }
        if (reportsConfig.getShowCustomerNumber()) {
        	headers.add("Kundnummer");
        }
        if (reportsConfig.getShowCustomerCity()) {
        	headers.add("Stad");
        }
        if (reportsConfig.getShowDeliveryAddress()) {
        	headers.add("Leveransadress namn");
        	headers.add("Leveransadress 1");
        	headers.add("Leveransadress 2");
        }
        if (reportsConfig.getShowContactPerson1()) {
        	headers.add("Kontaktperson 1 namn");
        	headers.add("Kontaktperson 1 epost");
        	headers.add("Kontaktperson 1 telefon");
        }
        if (reportsConfig.getShowContactPerson2()) {
        	headers.add("Kontaktperson 2 namn");
        	headers.add("Kontaktperson 2 epost");
        	headers.add("Kontaktperson 2 telefon");
        }
        
        // Lägg till Order customattribut
        for (CustomerCustomField customField : getCheckedCustomFields(searchBean.getCustomerCustomFields())) {
        	headers.add(customField.getLabel());
        }
        headers.add("Orderrad");
        headers.add("Artikelnr");
        headers.add("Artikelbeskrivning");
        headers.add("Leasingavtal orderrad");
        headers.add("Antal");
        headers.add("Registrerat");
        headers.add("Serienummer");
        headers.add("Stöld-ID");
        // Lägg till Equipment customattribut
        if (searchBean.getCustomerGroupId() != reqAttr.getZeroValue()) {
	        for (String eqHeader : getCustomerEquipmentFields()) {
	        	headers.add(eqHeader);
	        }
        }
        model.put("headers", headers);

        List<List<String>> results = new ArrayList<List<String>>();
        
        for (OrderHeader order: orders) {
        	order = orderRepo.getOne(order.getId());
        	for (OrderLine line : order.getOrderLines()) {
        		if (line.getHasSerialNo()) {
            		for (Equipment equipment : line.getEquipments()) {
            			List<String> orderCols = new ArrayList<String>();
            	        if (reportsConfig.getShowOrderNumber()) {
            	        	orderCols.add(order.getOrderNumber());
            	        }
            	        if (reportsConfig.getShowNetsetOrderNumber()) {
            	        	orderCols.add(order.getNetsetOrderNumber());
            	        }
            	        if (reportsConfig.getShowCustomerOrderNumber()) {
            	        	orderCols.add(order.getCustomerOrderNumber());
            	        }
            	        if (reportsConfig.getShowCustomerSalesOrder()) {
            	        	orderCols.add(order.getCustomerSalesOrder());
            	        }
            	        if (reportsConfig.getShowLeasingNumber()) {
            	        	orderCols.add(order.getLeasingNumber());
            	        }
            	        if (reportsConfig.getShowOrderDate()) {
            	        	orderCols.add(order.getOrderDateAsString());
            	        }
            	        if (reportsConfig.getShowDeliveryDate()) {
            	        	orderCols.add(order.getDeliveryDateDisplay());
            	        }
            	        if (reportsConfig.getShowCustomerName()) {
            	        	orderCols.add(order.getCustomerName());
            	        }
            	        if (reportsConfig.getShowCustomerNumber()) {
            	        	orderCols.add(order.getCustomerNumber());
            	        }
            	        if (reportsConfig.getShowCustomerCity()) {
            	        	orderCols.add(order.getCity());
            	        }
            	        if (reportsConfig.getShowDeliveryAddress()) {
            	        	orderCols.add(order.getDeliveryAddressName());
            	        	orderCols.add(order.getDeliveryPostalAddress1());
            	        	orderCols.add(order.getDeliveryPostalAddress2());
            	        }
            	        if (reportsConfig.getShowContactPerson1()) {
            	        	orderCols.add(order.getContact1Name());
            	        	orderCols.add(order.getContact1Email());
            	        	orderCols.add(order.getContact1Phone());
            	        }
            	        if (reportsConfig.getShowContactPerson2()) {
            	        	orderCols.add(order.getContact2Name());
            	        	orderCols.add(order.getContact2Email());
            	        	orderCols.add(order.getContact2Phone());
            	        }

                    	for (CustomerCustomField customField : getCheckedCustomFields(searchBean.getCustomerCustomFields())) {
                    		orderCols.add(getOrderCustomFieldValue(customField, order));
                    	}
                    	orderCols.add(String.valueOf(line.getRowNumber()));
                    	orderCols.add(line.getArticleNumber());
                    	orderCols.add(line.getArticleDescription());
                    	orderCols.add(line.getLeasingNumber());
                    	orderCols.add(String.valueOf(line.getTotal()));
                    	orderCols.add(String.valueOf(line.getRegistered()));
                    	orderCols.add(equipment.getSerialNo());
                    	orderCols.add(equipment.getStealingTag());                    	
                    	for (String customValue : getEquipmentFieldValues(equipment)) {
                    		orderCols.add(customValue);
                    	}                    	
                    	results.add(orderCols);            			
            		}        			
        		}
        	}        	
        }        
        model.put("results",results);
        
        return model;
		
	}

	private ReportsConfig getReportsConfig() {
		ReportsConfig config = new ReportsConfig();
		LOG.debug("searchBean.getCustomerGroupId(): " + searchBean.getCustomerGroupId());
		if (searchBean.getCustomerGroupId() > 0) {
			CustomerGroup customerGroup = customerRepo.getOne(searchBean.getCustomerGroupId());
			LOG.debug("customerGroup.getName(): " + customerGroup.getName());
			if (customerGroup != null && customerGroup.getReportsConfig() != null) {
				return customerGroup.getReportsConfig();
			}
		}
		return config;
	}
	
	private List<String> getCustomerEquipmentFields() {
		List<String> list = new ArrayList<String>();
		if (searchBean.getCustomerGroupId() > 0) {
			CustomerGroup customerGroup = customerRepo.getOne(searchBean.getCustomerGroupId());
			if (customerGroup != null && customerGroup.getRegistrationConfig() != null) {
				RegistrationConfig config = customerGroup.getRegistrationConfig();
				if (config.getUseAttribute1()) {
					list.add(config.getLabelAttribute1());
				}
				if (config.getUseAttribute2()) {
					list.add(config.getLabelAttribute2());
				}
				if (config.getUseAttribute3()) {
					list.add(config.getLabelAttribute3());
				}
				if (config.getUseAttribute4()) {
					list.add(config.getLabelAttribute4());
				}
				if (config.getUseAttribute5()) {
					list.add(config.getLabelAttribute5());
				}
				if (config.getUseAttribute6()) {
					list.add(config.getLabelAttribute6());
				}
				if (config.getUseAttribute7()) {
					list.add(config.getLabelAttribute7());
				}
				if (config.getUseAttribute8()) {
					list.add(config.getLabelAttribute8());
				}
			}
		}
		return list;
	}

	private List<String> getEquipmentFieldValues(Equipment equipment) {
		List<String> list = new ArrayList<String>();
		if (searchBean.getCustomerGroupId() > 0) {
			CustomerGroup customerGroup = customerRepo.getOne(searchBean.getCustomerGroupId());
			if (customerGroup != null && customerGroup.getRegistrationConfig() != null) {
				RegistrationConfig config = customerGroup.getRegistrationConfig();
				if (config.getUseAttribute1()) {
					list.add(equipment.getCustomAttribute1());
				}
				if (config.getUseAttribute2()) {
					list.add(equipment.getCustomAttribute2());
				}
				if (config.getUseAttribute3()) {
					list.add(equipment.getCustomAttribute3());
				}
				if (config.getUseAttribute4()) {
					list.add(equipment.getCustomAttribute4());
				}
				if (config.getUseAttribute5()) {
					list.add(equipment.getCustomAttribute5());
				}
				if (config.getUseAttribute6()) {
					list.add(equipment.getCustomAttribute6());
				}
				if (config.getUseAttribute7()) {
					list.add(equipment.getCustomAttribute7());
				}
				if (config.getUseAttribute8()) {
					list.add(equipment.getCustomAttribute8());
				}
			}
		}
		return list;
	}
	
	private String getCustomerNameFromsession() {
		String result = "Alla kunder";		
		CustomerGroup group = getCustomerGroupFromSession();
		if (group != null) {
			result = group.getName();
		}
		return result;
	}

	private String getCustomerEmailFromsession() {
		String result = "";		
		CustomerGroup group = getCustomerGroupFromSession();
		if (group != null) {
			result = group.getEmailAddress();
		}
		return result;
	}

	private CustomerGroup getCustomerGroupFromSession() {		
		CustomerGroup result = null;
		Long custGroupId = searchBean.getCustomerGroupId();		
		if (custGroupId > 0) {
			result = customerRepo.getOne(custGroupId);
		}
		return result;
	}

	private List<CustomerCustomField> getCustomerCustomFields(Long customerGroupId) {
		List<CustomerCustomField> result = null;
		if (customerGroupId != null && customerGroupId > 0) {
			CustomerGroup customerGroup = customerRepo.findOne(customerGroupId);
			if (customerGroup != null) {
				result = customerGroup.getCustomerCustomFields();
			}			
		}
		return result;
	}
	
	private List<CustomerCustomField> getCheckedCustomFields(List<CustomerCustomField> allFields) {
		List<CustomerCustomField> fields = new ArrayList<CustomerCustomField>();
		for (CustomerCustomField field : allFields) {
			if (field.getShowInDeliveryReport()) {
				fields.add(field);
			}
		}
		return fields;
	}
	private List<CustomerCustomField> getCheckedCustomerCustomFields(RequestAttributes reqAttr) {
		List<CustomerCustomField> custFields = getCustomerCustomFields(reqAttr.getCustomerId());
		List<CustomerCustomField> checkedFields = reqAttr.getCustomerCustomFields();
		List<CustomerCustomField> result = new ArrayList<CustomerCustomField>();
		for (CustomerCustomField custField : custFields) {
			for (CustomerCustomField checkedField : checkedFields) {
				if (custField.getId().equals(checkedField.getId())) {
					custField.setShowInDeliveryReport(checkedField.getShowInDeliveryReport());
					break;
				}
			}
			result.add(custField);
		}
		return result;
	}
	@Autowired
	public void setExcelGenerator(ExcelGenerator excelGenerator) {
		this.excelGenerator = excelGenerator;
	}
	@Autowired
	public void setAttachmentRepo(AttachmentRepository attachmentRepo) {
		this.attachmentRepo = attachmentRepo;
	}
	@Autowired
	public void setEmailRepo(EmailRepository emailRepo) {
		this.emailRepo = emailRepo;
	}	
	@Autowired
	public void setPropertyRepo(PropertyRepository propertyRepo) {
		this.propertyRepo = propertyRepo;
	}	
}
