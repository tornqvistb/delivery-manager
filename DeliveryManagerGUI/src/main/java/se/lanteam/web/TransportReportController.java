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
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SearchBean;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.ExcelViewBuilder;

@Controller
public class TransportReportController {
	
	private OrderRepository orderRepo;
	private SearchBean searchBean;
	
	@RequestMapping("reports/transport")
	public String showDeliveryPlanReport(ModelMap model) {
		model.put("reqAttr", new RequestAttributes());
		return "transport-report";
	}

	@RequestMapping(value="reports/transport/search", method=RequestMethod.GET)
	public String searchPlannedOrders(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		try {
			Date planDate = DateUtil.stringToDate(reqAttr.getPlanDate());
			List<OrderHeader> orders = orderRepo.findOrdersByPlanDate(planDate);
			model.put("orders", orders);
			searchBean.setOrderList(orders);
			searchBean.setFromDate(planDate);
			if (orders.isEmpty()) {
				reqAttr.setErrorMessage("Sökningen gav inga träffar");
			}
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}		
		model.put("reqAttr", reqAttr);
		return "transport-report";
	}
	@RequestMapping(value="reports/transport/export", method=RequestMethod.GET)
	public ModelAndView exportPlannedOrdersToExcel(ModelMap model, HttpServletResponse response) throws ParseException {
		
        //Sheet Name
        model.put("sheetname", "transport-report");
        //Headers List
        List<String> headers = new ArrayList<String>();
		
        headers.add("Order");
        headers.add("Leveransnr. kund");
        headers.add("Kund");
        headers.add("Område");
        headers.add("Address 1");
        headers.add("Address 2");
        headers.add("Artikelnummer");
        headers.add("Artikelbeskrivning");
        headers.add("Antal");
        headers.add("Kommentar");
        model.put("headers", headers);
        
        List<String> numericColumns = new ArrayList<String>();
        numericColumns.add("Antal");
        model.put("numericcolumns", numericColumns);

        List<List<String>> results = new ArrayList<List<String>>();
        
        List<OrderHeader> orders = searchBean.getOrderList();
        
        for (OrderHeader order: orders) {
        	boolean firstRow = true;
        	order = orderRepo.getOne(order.getId());
        	for (OrderLine orderLine : order.getOrderLines()) {
	        	List<String> orderCols = new ArrayList<String>();
	        	orderCols.add(order.getOrderNumber());
	        	if (firstRow) {		        	
		        	orderCols.add(order.getCustomerSalesOrder());
		        	orderCols.add(order.getCustomerName());
		        	orderCols.add(order.getDeliveryPlan().getDeliveryArea().getName());
		        	orderCols.add(order.getDeliveryPostalAddress1());
		        	orderCols.add(order.getDeliveryPostalAddress2());		        	
	        	} else {
		        	orderCols.add("");
		        	orderCols.add("");
		        	orderCols.add("");
		        	orderCols.add("");
		        	orderCols.add("");		        		        		
	        	}
	        	orderCols.add(orderLine.getArticleNumber());
	        	orderCols.add(orderLine.getArticleDescription());
	        	orderCols.add(String.valueOf(orderLine.getTotal()));
	        	if (firstRow) {
	        		orderCols.add(order.getDeliveryPlan().getComment());
	        	} else {
	        		orderCols.add("");
	        	}
	        	results.add(orderCols);
	        	firstRow = false;
        	}
        }
        
        model.put("results",results);
        model.put("pageheader", DateUtil.dateToString(searchBean.getFromDate()));
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=" + "transport-report-" + DateUtil.dateToString(new Date())+ ".xls" );         
		
		return new ModelAndView(new ExcelViewBuilder(), model);
	}

	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

}
