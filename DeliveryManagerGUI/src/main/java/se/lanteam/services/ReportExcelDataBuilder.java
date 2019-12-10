package se.lanteam.services;

import static se.lanteam.model.DeliveryReportLine.COL_ARTICLE_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOMER_ORDER_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOMER_SALES_ORDER;
import static se.lanteam.model.DeliveryReportLine.COL_CUST_ROW_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_LABEL_1;
import static se.lanteam.model.DeliveryReportLine.COL_ORDER_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_SERIAL_NO;
import static se.lanteam.model.DeliveryReportLine.COL_STEALING_TAG;
import static se.lanteam.model.DeliveryReportLine.COL_TOTAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.DeliveryReportField;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.ReportsConfig;
import se.lanteam.model.DeliveryReportLine;
import se.lanteam.model.SearchBean;
import se.lanteam.model.comparator.SortByEQCustomLabel1;
import se.lanteam.model.comparator.SortByEQSerialNo;
import se.lanteam.model.comparator.SortByEQStealingTag;
import se.lanteam.model.comparator.SortByOHCustomerOrderNumber;
import se.lanteam.model.comparator.SortByOHCustomerSalesOrder;
import se.lanteam.model.comparator.SortByOHOrderNumber;
import se.lanteam.model.comparator.SortByOLArticleNumber;
import se.lanteam.model.comparator.SortByOLCustomerRowNumber;
import se.lanteam.model.comparator.SortByOLTotal;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;


@Service
public class ReportExcelDataBuilder {
	
	@Autowired
	private CustomerGroupRepository customerRepo;
	@Autowired
	private OrderRepository orderRepo;
		
	public ModelMap getExcelDataIntoModel(ModelMap model, SearchBean searchBean) {
		
        model.put("sheetname", "Leveransrapport");
        
        CustomerGroup customerGroup = customerRepo.getOne(searchBean.getCustomerGroupId());
        ReportsConfig reportsConfig = customerGroup.getReportsConfig();
        
        List<String> headers = new ArrayList<>();
        
        for (DeliveryReportField field : reportsConfig.getActiveReportFields()) {
        	headers.add(field.getLabel());
        }

        model.put("headers", headers);

        List<DeliveryReportLine> reportLines = new ArrayList<DeliveryReportLine>();
		List<OrderHeader> orders = searchBean.getOrderList();
		
		for (OrderHeader order: orders) {
        	order = orderRepo.getOne(order.getId());
        	for (OrderLine line : order.getOrderLines()) {
        		if (line.getHasSerialNo()) {
            		for (Equipment equipment : line.getEquipments()) {
            			reportLines.add(getReportLine(order, line, equipment));
            		}        			
        		}
        	}        	
        }        
		reportLines = sort(reportLines, reportsConfig);
        List<List<String>> results = new ArrayList<List<String>>();
		List<String> orderCols = new ArrayList<String>();
		results.add(orderCols);
        model.put("results", getResults(reportLines, reportsConfig));
        
        return model;
	}

	private List<List<String>> getResults(List<DeliveryReportLine> reportLines, ReportsConfig reportsConfig) {
		List<List<String>> results = new ArrayList<List<String>>();
		for (DeliveryReportLine line : reportLines) {
			List<String> orderCols = new ArrayList<String>();
			for (DeliveryReportField field : reportsConfig.getActiveReportFields()) {
				if (COL_ORDER_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOhOrderNumber());
				}
				if (COL_CUSTOMER_ORDER_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomerOrderNumber());
				}
				if (COL_CUSTOMER_SALES_ORDER.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomerSalesOrder());
				}
				if (COL_ARTICLE_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOlArticleNumber());
				}
				if (COL_TOTAL.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlTotal()));
				}
				if (COL_CUST_ROW_NUMBER.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlCustomerRowNumber()));
				}
				if (COL_SERIAL_NO.equals(field.getFieldName())) {
					orderCols.add(line.getEqSerialNo());
				}
				if (COL_STEALING_TAG.equals(field.getFieldName())) {
					orderCols.add(line.getEqStealingTag());
				}
				if (COL_EQ_CUST_LABEL_1.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomLabel1());
				}
			}
			results.add(orderCols);
		}
		return results;
	}
	
	private List<DeliveryReportLine> sort(List<DeliveryReportLine> lines, ReportsConfig reportsConfig) {
		
		if (COL_ORDER_NUMBER.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByOHOrderNumber());
		} else if (COL_CUSTOMER_ORDER_NUMBER.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByOHCustomerOrderNumber());
		} else if (COL_CUSTOMER_SALES_ORDER.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByOHCustomerSalesOrder());
		} else if (COL_ARTICLE_NUMBER.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByOLArticleNumber());
		} else if (COL_TOTAL.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByOLTotal());
		} else if (COL_CUST_ROW_NUMBER.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByOLCustomerRowNumber());
		} else if (COL_SERIAL_NO.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByEQSerialNo());
		} else if (COL_STEALING_TAG.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByEQStealingTag());
		} else if (COL_EQ_CUST_LABEL_1.equals(reportsConfig.getSortColumnDeliverReport())) {
			Collections.sort(lines, new SortByEQCustomLabel1());
		}
		 
		return lines;
	}	
	
	private DeliveryReportLine getReportLine(OrderHeader order, OrderLine orderLine, Equipment equipment) {
		DeliveryReportLine line = new DeliveryReportLine();
		
		line.setOhCustomerOrderNumber(order.getCustomerOrderNumber());
		line.setOhCustomerSalesOrder(order.getCustomerSalesOrder());
		line.setOhOrderNumber(order.getOrderNumber());
		line.setOlArticleNumber(orderLine.getArticleNumber());
		line.setOlCustomerRowNumber(orderLine.getCustomerRowNumber());
		line.setOlTotal(orderLine.getTotal());
		line.setEqSerialNo(equipment.getSerialNo());
		line.setEqStealingTag(equipment.getStealingTag());
		line.setEqCustomLabel1(equipment.getCustomAttribute1());
		
		return line;
	}
	
	protected String getOrderCustomFieldValue(CustomerCustomField customField, OrderHeader order) {
		String value = "";
		for (OrderCustomField orderCustomField : order.getOrderCustomFields()) {
			if (orderCustomField.getCustomField().getIdentification() == customField.getCustomField().getIdentification()) {
				value = orderCustomField.getValue();
				break;
			}
		}
		return value;
	}

}