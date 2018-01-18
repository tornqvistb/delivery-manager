package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.StatusConstants;
import se.lanteam.constants.StatusUtil;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.ExcelViewBuilder;

@Controller
public class SLAReportController extends BaseController {
	
	@RequestMapping("reports/sla")
	public String showSlaReport(ModelMap model) {
		model.put("customerGroups", customerRepo.findAll());
		model.put("reqAttr", new RequestAttributes());
		return "sla-report";
	}
	@RequestMapping(value="reports/sla/search", method=RequestMethod.GET)
	public String searchOrdersSla(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		String status = reqAttr.getOrderStatus();
		
		try {
			Date fromDate = DateUtil.getDefaultStartDate();
			if (!StringUtils.isEmpty(reqAttr.getFromDate())) {
				fromDate = DateUtil.stringToDate(reqAttr.getFromDate());
			}
			
			Date toDate = DateUtil.getTomorrow();
			if (!StringUtils.isEmpty(reqAttr.getToDate())) {
				toDate = DateUtil.stringToDate(reqAttr.getToDate());
			}
			Long customerGroupId = reqAttr.getCustomerId();
			Date orderDate = DateUtil.getDefaultStartDate();
			List<OrderHeader> orders;
			List<String> stati = new ArrayList<String>();
			if (status.equals(StatusConstants.ORDER_STATUS_GROUP_ACTIVE)){
				stati = Arrays.asList(StatusConstants.ACTIVE_STATI);
			} else if (status.equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
				stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
			} else {
				stati.add(status);
			}
			if (StatusUtil.isActiveStatus(status)) {
				if (customerGroupId > 0) {
					orders = orderRepo.findOrdersFromSearchSLAByCustGroup(stati, customerGroupId);
				} else {
					orders = orderRepo.findOrdersFromSearchSLA(stati);
				}
			} else {
				if (customerGroupId > 0) {
					orders = orderRepo.findDeliveredOrdersFromSearchSLA(stati, orderDate, fromDate, toDate);
				} else {
					orders = orderRepo.findDeliveredOrdersFromSearchSLAByCustGroup(stati, orderDate, fromDate, toDate, customerGroupId);
				}
			}
			
			//sort by sla days
			Collections.sort(orders, new Comparator<OrderHeader>() {
				@Override
				public int compare(OrderHeader o1, OrderHeader o2) {
					return o1.getSlaDaysLeft() - o2.getSlaDaysLeft();
				}
			});
			
			model.put("orders", orders);
			searchBean.setOrderList(orders);
			searchBean.setCustomerGroupId(customerGroupId);
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}		
		reqAttr.setOrderStatus(status);
		model.put("reqAttr", reqAttr);
		model.put("customerGroups", customerRepo.findAll());
		return "sla-report";
	}
	
	@RequestMapping(value="reports/sla/export", method=RequestMethod.GET)
	public ModelAndView exportSlaToExcel(ModelMap model, HttpServletResponse response) throws ParseException {
		
        //Sheet Name
        model.put("sheetname", "sla-report");
        //Headers List
        List<String> headers = new ArrayList<String>();
		
        headers.add("Ordernummer");
        headers.add("Leveransnummer kund");
        headers.add("Kund");
        headers.add("Orderdatum");
        headers.add("Leveransdatum");
        headers.add("Status");
        for (CustomerCustomField customField : getCustomerCustomFields()) {
        	headers.add(customField.getLabel());
        }
        headers.add("Dagar kvar");        
        
        model.put("headers", headers);
        
        List<String> numericColumns = new ArrayList<String>();
        numericColumns.add("Dagar kvar");
        model.put("numericcolumns", numericColumns);

        List<List<String>> results = new ArrayList<List<String>>();
        
        List<OrderHeader> orders = searchBean.getOrderList();
        
        for (OrderHeader order: orders) {
        	order = orderRepo.findOne(order.getId());
        	List<String> orderCols = new ArrayList<String>();
        	orderCols.add(order.getOrderNumber());
        	orderCols.add(order.getCustomerSalesOrder());
        	orderCols.add(order.getCustomerName());
        	orderCols.add(order.getOrderDateAsString());
        	orderCols.add(order.getDeliveryDateDisplay());
        	orderCols.add(order.getStatusDisplay());
        	for (CustomerCustomField customField : getCustomerCustomFields()) {
        		orderCols.add(getOrderCustomFieldValue(customField, order));
        	}
        	orderCols.add(String.valueOf(order.getSlaDaysLeft()));
        	results.add(orderCols);
        }
        
        model.put("results",results);
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=" + "sla-report-" + DateUtil.dateToString(new Date())+ ".xls" );         
		
		return new ModelAndView(new ExcelViewBuilder(), model);
	}
/*	
	private List<CustomerCustomField> getCustomerCustomFields() {
		List<CustomerCustomField> result = new ArrayList<CustomerCustomField>();
		CustomerGroup custGroup = customerRepo.findOne(searchBean.getCustomerGroupId());
		for (CustomerCustomField field : custGroup.getCustomerCustomFields()) {
			if (field.getShowInSlaReport()) {
				result.add(field);
			}
		}
		return result;
	}
	
	private String getOrderCustomFieldValue(CustomerCustomField customField, OrderHeader order) {
		String value = "";
		for (OrderCustomField orderCustomField : order.getOrderCustomFields()) {
			if (orderCustomField.getCustomField().getIdentification() == customField.getCustomField().getIdentification()) {
				value = orderCustomField.getValue();
				break;
			}
		}
		return value;
	}
*/
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}

}
