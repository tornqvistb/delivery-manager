package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.FileConstants;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.CustomerNumber;
import se.lanteam.domain.Email;
import se.lanteam.domain.OrderHeader;
import se.lanteam.exceptions.MissingInputException;
import se.lanteam.model.OrderListSearchBean;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.AttachmentRepository;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.PropertyRepository;
import se.lanteam.services.ExcelGenerator;
import se.lanteam.services.ExcelViewBuilder;
import se.lanteam.services.PropertyService;
import se.lanteam.services.ReportExcelDataBuilder;

@Controller
public class DeliveryReportController extends BaseController{
	
	private ExcelGenerator excelGenerator;
	private AttachmentRepository attachmentRepo;
	private EmailRepository emailRepo;
	private PropertyRepository propertyRepo;
	private PropertyService propService;
	private ReportExcelDataBuilder dataBuilder;
	private OrderListSearchBean orderListSearchBean = new OrderListSearchBean();
	
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
		reqAttr.setCustomerNumbers(getCustomerNumbers(customerId));
		model.put("reqAttr", reqAttr);
		model.put("customerGroups", customerRepo.findAll());
		return "delivery-report";
	}
	
	private List<CustomerNumber> getCustomerNumbers(Long customerId) {
		List<CustomerNumber> customerNumbers = new ArrayList<>();
		CustomerGroup group = customerRepo.findById(customerId);
		if (group != null && group.getCustomerNumbers() != null) {
			customerNumbers = new ArrayList<>(group.getCustomerNumbers());
		}
		return customerNumbers;
	}
	
	@RequestMapping(value="reports/delivery/search", method=RequestMethod.GET)
	public String searchOrdersDelivery(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		try {
			if (reqAttr.getCustomerId() == 0) {
				throw new MissingInputException("Du måste välja kundgrupp");
			}			
			orderListSearchBean.setQuery(reqAttr.getQuery());
			orderListSearchBean.setFromDate(reqAttr.getFromDate());
			orderListSearchBean.setToDate(reqAttr.getToDate());
			orderListSearchBean.setStatus(reqAttr.getOrderStatus());
			orderListSearchBean.setCustomerGroupId(reqAttr.getCustomerId());
			orderListSearchBean.setCustomerNumber(reqAttr.getCustomerNumber());			
			List<OrderHeader> orders = search();
			if (!orders.isEmpty()) {
				reqAttr.setResultNotEmptyMsg("Sökningen gav träff på " + orders.size() + " ordrar");
			} else {
				reqAttr.setResultEmptyMsg("Sökningen gav inga träffar");
			}
			reqAttr.setCustomerNumbers(getCustomerNumbers(reqAttr.getCustomerId()));
			model.put("reqAttr", reqAttr);
			model.put("orders", orders);
			model.put("customerGroups", customerRepo.findAll());
			Date fromDate = StringUtils.hasLength(orderListSearchBean.getFromDate()) ? DateUtil.stringToDate(orderListSearchBean.getFromDate()) : null;
			Date toDate = StringUtils.hasLength(orderListSearchBean.getToDate()) ? DateUtil.stringToDate(orderListSearchBean.getToDate()) : null;
			searchBean.populate(orders, reqAttr.getCustomerId(), fromDate, toDate, reqAttr.getCustomerCustomFields());
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
	
	private List<OrderHeader> search() throws ParseException {
		populateDatesInBean();
		List<String> stati = new ArrayList<String>();
		orderListSearchBean.setMaxRows(new PageRequest(0, propService.getLong(PropertyConstants.MAX_ORDERS_IN_SEARCH).intValue()));
		if (datesAreEmpty()) {
			if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_ACTIVE)){
				stati = Arrays.asList(StatusConstants.ACTIVE_STATI);
			} else if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
				stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
			} else if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_ALL)) {
				stati = Arrays.asList(StatusConstants.ALL_STATI);
			} else {
				stati.add(orderListSearchBean.getStatus());
			}	
			orderListSearchBean.setStati(stati);
			return orderRepo.findOrdersFromSearchDeliveryReport(orderListSearchBean.getStati(), 
					orderListSearchBean.getQueryWithWildcards(), 
					orderListSearchBean.getCustomerGroupId(), 
					getNullIfEmpty(orderListSearchBean.getCustomerNumber()),
					orderListSearchBean.getMaxRows());
		} else {
			if (!Arrays.asList(StatusConstants.INACTIVE_STATI).contains(orderListSearchBean.getStatus())) {
				orderListSearchBean.setStatus(StatusConstants.ORDER_STATUS_GROUP_INACTIVE);
				stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
			} else {
				stati.add(orderListSearchBean.getStatus());
			}
			orderListSearchBean.setStati(stati);
			return orderRepo.findDeliveredOrdersFromSearchDeliveryReport(orderListSearchBean.getStati(), 
					orderListSearchBean.getQueryWithWildcards(), 
					DateUtil.stringToDateAfterMidnight(orderListSearchBean.getFromDate()),
					DateUtil.stringToDateMidnight(orderListSearchBean.getToDate()),
					orderListSearchBean.getCustomerGroupId(), 
					getNullIfEmpty(orderListSearchBean.getCustomerNumber()),
					orderListSearchBean.getMaxRows());
		}
	}
	
	private String getNullIfEmpty (String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		return s;
	}
	
	private void populateDatesInBean() {
		if (!datesAreEmpty()) {
			if (StringUtils.isEmpty(orderListSearchBean.getFromDate())) {
				orderListSearchBean.setFromDate(DateUtil.getDefaultStartDateAsString());
			}
			if (StringUtils.isEmpty(orderListSearchBean.getToDate())) {
				orderListSearchBean.setToDate(DateUtil.getTomorrowAsString());
			}
		}
	}
	
	private boolean datesAreEmpty() {
		return StringUtils.isEmpty(orderListSearchBean.getFromDate())
				&& StringUtils.isEmpty(orderListSearchBean.getToDate());
	}
	
	@RequestMapping(value="reports/delivery/export", method=RequestMethod.GET)
	public ModelAndView exportDeliveryToExcel(ModelMap model, HttpServletResponse response, @ModelAttribute RequestAttributes reqAttr) throws ParseException {
		model = dataBuilder.getExcelDataIntoModel(model, searchBean);
        response.setContentType( "application/ms-excel" );
        String fileName = EXCEL_EXPORT_FILE_NAME.replace("#customer", getCustomerNameFromsession());
        response.setHeader( "Content-disposition", "attachment; filename=" + fileName);         
		
		return new ModelAndView(new ExcelViewBuilder(), model);
	}

	@RequestMapping(value="reports/delivery/informCustomer", method=RequestMethod.GET)
	public String informCustomer(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {			
		model = dataBuilder.getExcelDataIntoModel(model, searchBean);
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
	public void setReportExcelDataBuilder(ReportExcelDataBuilder dataBuilder) {
		this.dataBuilder = dataBuilder;
	}	
	@Autowired
	public void setPropertyService(PropertyService propService) {
		this.propService = propService;
	}
	
}
