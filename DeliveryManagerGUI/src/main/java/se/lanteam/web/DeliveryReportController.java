package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.FileConstants;
import se.lanteam.constants.LimStringUtil;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.CustomField;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.Email;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SearchBean;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.AttachmentRepository;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.repository.PropertyRepository;
import se.lanteam.services.ExcelGenerator;
import se.lanteam.services.ExcelViewBuilder;

@Controller
public class DeliveryReportController {
	
	private OrderRepository orderRepo;
	private CustomerGroupRepository customerRepo;
	private SearchBean searchBean;
	private ExcelGenerator excelGenerator;
	private AttachmentRepository attachmentRepo;
	private EmailRepository emailRepo;
	private PropertyRepository propertyRepo;
	private SessionBean sessionBean;
	
	private static final String EXCEL_EXPORT_FILE_NAME = "Leveransrapport-#customer-" + DateUtil.dateToString(new Date()) + FileConstants.FILE_ENDING_EXCEL;
	private static final String MAIL_SUBJECT = "Leveransrapport från LanTeam";
	
	@RequestMapping("reports/delivery")
	public String showDeliveryReport(ModelMap model) {
		model.put("customerGroups", customerRepo.findAll());
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr.setCustomerCustomFields(sessionBean.getCustomerGroup().getCustomerCustomFields());
		model.put("reqAttr", reqAttr);
		return "delivery-report";
	}

	@RequestMapping(value="reports/delivery/search", method=RequestMethod.GET)
	public String searchOrdersDelivery(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		try {
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
			reqAttr.setCustomerCustomFields(updateFieldListWithLabels(reqAttr.getCustomerCustomFields()));					
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
		}
		return "delivery-report";
	}

	@RequestMapping(value="reports/delivery/export", method=RequestMethod.GET)
	public ModelAndView exportDeliveryToExcel(ModelMap model, HttpServletResponse response) throws ParseException {
		model = getExcelDataIntoModel(model);
        response.setContentType( "application/ms-excel" );
        String fileName = EXCEL_EXPORT_FILE_NAME.replace("#customer", getCustomerNameFromsession());
        response.setHeader( "Content-disposition", "attachment; filename=" + fileName);         
		
		return new ModelAndView(new ExcelViewBuilder(), model);
	}

	@RequestMapping(value="reports/delivery/informCustomer", method=RequestMethod.GET)
	public String informCustomer(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {			
		model = getExcelDataIntoModel(model);
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
				sb.append("Bifogat finner ni en rapport med utförda leveranser från LanTeam till " + getCustomerNameFromsession() + ".\n\n");
				sb.append("Datumintervall för leveranser i denna rapport: " + DateUtil.dateToString(searchBean.getFromDate()) + " - " + DateUtil.dateToString(searchBean.getToDate()) + "\n");
				sb.append("Ordernummerintervall för leveranser i denna rapport: " + searchBean.getFromOrderNo() + " - " + searchBean.getToOrderNo() + "\n\n");
				sb.append("Med vänlig hälsning\n");
				sb.append("LanTeam");								
				email.setContent(sb.toString());
				email.setSender(propertyRepo.findById(PropertyConstants.MAIL_USERNAME).getStringValue());
				email.setReceiver(getCustomerEmailFromsession());
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

	private ModelMap getExcelDataIntoModel(ModelMap model) {
		List<OrderHeader> orders = searchBean.getOrderList();
		
        //Sheet Name
        model.put("sheetname", "Leveransrapport");
        //Headers List
        List<String> headers = new ArrayList<String>();
		
        headers.add("Ordernummer");
        headers.add("Leveransnummer kund");
        headers.add("Kund");
        headers.add("Orderdatum");
        headers.add("Leveransdatum");
        // Lägg till Order customattribut
        List<CustomField> customFields = getCustomFieldsFromSession();
        for (CustomField customField : customFields) {
        	headers.add(customField.getLabel());
        }
        
        headers.add("Orderrad");
        headers.add("Artikelnr");
        headers.add("Artikelbeskrivning");
        headers.add("Antal");
        headers.add("Registrerat");
        headers.add("Serienummer");
        headers.add("Stöld-ID");
        // Lägg till Equipment customattribut
        for (String eqHeader : getCustomerEquipmentFields()) {
        	headers.add(eqHeader);
        }
        
        model.put("headers", headers);
        
        List<String> numericColumns = new ArrayList<String>();
        numericColumns.add("Ordernummer");
        model.put("numericcolumns", numericColumns);

        List<List<String>> results = new ArrayList<List<String>>();
        
        for (OrderHeader order: orders) {
        	order = orderRepo.getOne(order.getId());
        	for (OrderLine line : order.getOrderLines()) {
        		if (line.getHasSerialNo()) {
            		for (Equipment equipment : line.getEquipments()) {
            			List<String> orderCols = new ArrayList<String>();
                    	orderCols.add(order.getOrderNumber());
                    	orderCols.add(order.getCustomerSalesOrder());
                    	orderCols.add(order.getCustomerName());
                    	orderCols.add(order.getOrderDateAsString());
                    	orderCols.add(order.getDeliveryDateDisplay());
                    	for (CustomField customField : customFields) {
                    		orderCols.add(getOrderCustomFieldValue(customField, order));
                    	}
                    	orderCols.add(String.valueOf(line.getRowNumber()));
                    	orderCols.add(line.getArticleNumber());
                    	orderCols.add(line.getArticleDescription());
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

	private List<String> getCustomerEquipmentFields() {
		List<String> list = new ArrayList<String>();
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
		return list;
	}

	private List<String> getEquipmentFieldValues(Equipment equipment) {
		List<String> list = new ArrayList<String>();
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
		return list;
	}

	private List<CustomField> getCustomFieldsFromSession() {
		List<CustomField> customFields = new ArrayList<CustomField>();
    	for (CustomerCustomField customerCustomField : searchBean.getCustomerCustomFields()) {
    		if (customerCustomField.getShowInDeliveryReport()) {
    			customFields.add(customerCustomField.getCustomField());	    	        
    		}        		
    	}
	    return customFields;
	}

	
	private String getOrderCustomFieldValue(CustomField customField, OrderHeader order) {
		String value = "";
		for (OrderCustomField orderCustomField : order.getOrderCustomFields()) {
			if (orderCustomField.getCustomField().getIdentification() == customField.getIdentification()) {
				value = orderCustomField.getValue();
				break;
			}
		}
		return value;
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

	private List<CustomerCustomField> updateFieldListWithLabels(List<CustomerCustomField> customerFields) {
		CustomerGroup custGroup = customerRepo.getOne(sessionBean.getCustomerGroup().getId());
		int index = 0;
		for (CustomerCustomField field : customerFields) {
			field.setCustomField(custGroup.getCustomerCustomFields().get(index).getCustomField());
			index++;
		}
		return customerFields;
	}
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	@Autowired
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
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
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
