package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.LimStringUtil;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SearchBean;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.ExcelViewBuilder;

@Controller
public class DeliveryReportController {
	
	private OrderRepository orderRepo;
	private CustomerGroupRepository customerRepo;
	private SearchBean searchBean;
	
	@RequestMapping("reports/delivery")
	public String showDeliveryReport(ModelMap model) {
		model.put("customerGroups", customerRepo.findAll());
		model.put("reqAttr", new RequestAttributes());
		return "delivery-report";
	}

	@RequestMapping(value="reports/delivery/search", method=RequestMethod.GET)
	public String searchOrdersDelivery(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		try {
			Date fromDate = DateUtil.stringToDate(LimStringUtil.NVL(reqAttr.getFromDate(), DateUtil.getDefaultStartDateAsString()));
			Date toDate = DateUtil.stringToDateMidnight(LimStringUtil.NVL(reqAttr.getToDate(), DateUtil.getOneYearAheadAsString()));
			String fromOrderNo = LimStringUtil.NVL(reqAttr.getFromOrderNo(), LimStringUtil.firstOrderNo);
			String toOrderNo = LimStringUtil.NVL(reqAttr.getToOrderNo(), LimStringUtil.lastOrderNo);
			
			List<OrderHeader> orders;
			if (reqAttr.getCustomerId() != reqAttr.getZeroValue()) {
				orders = orderRepo.findDeliveredOrdersByCustGroup(fromDate, toDate, fromOrderNo, toOrderNo, reqAttr.getCustomerId());
			} else {
				orders = orderRepo.findDeliveredOrders(fromDate, toDate, fromOrderNo, toOrderNo);
			}
					
			if (!orders.isEmpty()) {
				reqAttr.setResultNotEmptyMsg("Sökningen gav " + orders.size() + " träff(ar)");
			} else {
				reqAttr.setResultEmptyMsg("Sökningen gav inga träffar");
			}
			model.put("reqAttr", reqAttr);
			model.put("orders", orders);
			model.put("customerGroups", customerRepo.findAll());
			searchBean.setOrderList(orders);
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}
		return "delivery-report";
	}

	@RequestMapping(value="reports/delivery/export", method=RequestMethod.GET)
	public ModelAndView exportDeliveryToExcel(ModelMap model, HttpServletResponse response) throws ParseException {
		
		List<OrderHeader> orders = searchBean.getOrderList();
		
        //Sheet Name
        model.put("sheetname", "delivery-report");
        //Headers List
        List<String> headers = new ArrayList<String>();
		
        headers.add("Ordernummer");
        headers.add("Leveransnummer kund");
        headers.add("Kund");
        headers.add("Orderdatum");
        headers.add("Leveransdatum");
        model.put("headers", headers);
        
        List<String> numericColumns = new ArrayList<String>();
        numericColumns.add("Ordernummer");
        model.put("numericcolumns", numericColumns);

        //Results Table (List<Object[]>)
        List<List<String>> results = new ArrayList<List<String>>();
        
        for (OrderHeader order: orders) {
        	List<String> orderCols = new ArrayList<String>();
        	orderCols.add(order.getOrderNumber());
        	orderCols.add(order.getCustomerSalesOrder());
        	orderCols.add(order.getCustomerName());
        	orderCols.add(order.getOrderDateAsString());
        	orderCols.add(order.getDeliveryDateDisplay());
        	results.add(orderCols);
        }
        
        model.put("results",results);
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=" + "delivery-report-" + DateUtil.dateToString(new Date())+ ".xls" );         
		
		return new ModelAndView(new ExcelViewBuilder(), model);
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
}
