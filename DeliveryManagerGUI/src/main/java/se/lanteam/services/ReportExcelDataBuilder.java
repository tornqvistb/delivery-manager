package se.lanteam.services;

import static se.lanteam.model.DeliveryReportLine.COL_ARTICLE_DESCRIPION;
import static se.lanteam.model.DeliveryReportLine.COL_ARTICLE_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_CITY;
import static se.lanteam.model.DeliveryReportLine.COL_CONTACT_1_EMAIL;
import static se.lanteam.model.DeliveryReportLine.COL_CONTACT_1_NAME;
import static se.lanteam.model.DeliveryReportLine.COL_CONTACT_1_PHONE;
import static se.lanteam.model.DeliveryReportLine.COL_CONTACT_2_EMAIL;
import static se.lanteam.model.DeliveryReportLine.COL_CONTACT_2_NAME;
import static se.lanteam.model.DeliveryReportLine.COL_CONTACT_2_PHONE;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOMER_NAME;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOMER_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOMER_ORDER_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOMER_SALES_ORDER;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_1;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_10;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_2;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_3;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_4;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_5;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_6;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_7;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_8;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTOM_FIELD_9;
import static se.lanteam.model.DeliveryReportLine.COL_CUSTUMER_ROW_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_DELIVERY_ADDRESS;
import static se.lanteam.model.DeliveryReportLine.COL_DELIVERY_CITY;
import static se.lanteam.model.DeliveryReportLine.COL_DELIVERY_DATE;
import static se.lanteam.model.DeliveryReportLine.COL_DELIVERY_POSTAL_ADDRESS_1;
import static se.lanteam.model.DeliveryReportLine.COL_DELIVERY_POSTAL_ADDRESS_2;
import static se.lanteam.model.DeliveryReportLine.COL_DELIVERY_POSTAL_CODE;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_1;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_2;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_3;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_4;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_5;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_6;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_7;
import static se.lanteam.model.DeliveryReportLine.COL_EQ_CUST_ATTRIBUTE_8;
import static se.lanteam.model.DeliveryReportLine.COL_INSTALLATION_TYPE;
import static se.lanteam.model.DeliveryReportLine.COL_JOINT_DELIVERY;
import static se.lanteam.model.DeliveryReportLine.COL_NETSET_ORDER_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_OH_LEASING_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_OL_LEASING_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_OPERATING_SYSTEM;
import static se.lanteam.model.DeliveryReportLine.COL_ORDER_DATE;
import static se.lanteam.model.DeliveryReportLine.COL_ORDER_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_OU;
import static se.lanteam.model.DeliveryReportLine.COL_POSTAL_ADDRESS_1;
import static se.lanteam.model.DeliveryReportLine.COL_POSTAL_ADDRESS_2;
import static se.lanteam.model.DeliveryReportLine.COL_POSTAL_CODE;
import static se.lanteam.model.DeliveryReportLine.COL_REGISTERED;
import static se.lanteam.model.DeliveryReportLine.COL_REMAINING;
import static se.lanteam.model.DeliveryReportLine.COL_RITM;
import static se.lanteam.model.DeliveryReportLine.COL_ROW_NUMBER;
import static se.lanteam.model.DeliveryReportLine.COL_SERIAL_NO;
import static se.lanteam.model.DeliveryReportLine.COL_STATUS;
import static se.lanteam.model.DeliveryReportLine.COL_STEALING_TAG;
import static se.lanteam.model.DeliveryReportLine.COL_TOTAL;
import static se.lanteam.model.DeliveryReportLine.COL_TRANSFER_DATE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import se.lanteam.constants.DateUtil;
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
import se.lanteam.model.comparator.DeliveryReportSorter;
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
        		if (line.getHasSerialNo() && line.getEquipments() != null && line.getEquipments().size() > 0) {
            		for (Equipment equipment : line.getEquipments()) {
            			reportLines.add(getReportLine(order, line, equipment));
            		}        			
        		} else if (line.getCustomerRowNumber() > 0) {
        			reportLines.add(getReportLine(order, line, null));
        		}
        	}        	
        }        
		Collections.sort(reportLines, new DeliveryReportSorter(reportsConfig.getSortColumnDeliverReport()));
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
				if (COL_NETSET_ORDER_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOhNetsetOrderNumber());
				}
				if (COL_OH_LEASING_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOhLeasingNumber());					
				}
				if (COL_CUSTOMER_NAME.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomerName());					
				}
				if (COL_CUSTOMER_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomerNumber());					
				}
				if (COL_DELIVERY_ADDRESS.equals(field.getFieldName())) {
					orderCols.add(line.getOhDeliveryAddress());					
				}
				if (COL_DELIVERY_CITY.equals(field.getFieldName())) {
					orderCols.add(line.getOhDeliveryCity());					
				}
				if (COL_DELIVERY_DATE.equals(field.getFieldName())) {
					orderCols.add(line.getOhDeliveryDate());					
				}
				if (COL_DELIVERY_POSTAL_ADDRESS_1.equals(field.getFieldName())) {
					orderCols.add(line.getOhDeliveryPostalAddress1());										
				}
				if (COL_DELIVERY_POSTAL_ADDRESS_2.equals(field.getFieldName())) {
					orderCols.add(line.getOhDeliveryPostalAddress2());					
				}
				if (COL_DELIVERY_POSTAL_CODE.equals(field.getFieldName())) {
					orderCols.add(line.getOhDeliveryPostalCode());					
				}
				if (COL_CITY.equals(field.getFieldName())) {
					orderCols.add(line.getOhCity());					
				}
				if (COL_POSTAL_ADDRESS_1.equals(field.getFieldName())) {
					orderCols.add(line.getOhPostalAddress1());					
				}
				if (COL_POSTAL_ADDRESS_2.equals(field.getFieldName())) {
					orderCols.add(line.getOhPostalAddress2());					
				}
				if (COL_POSTAL_CODE.equals(field.getFieldName())) {
					orderCols.add(line.getOhPostalCode());					
				}
				if (COL_CONTACT_1_EMAIL.equals(field.getFieldName())) {					
					orderCols.add(line.getOhContact1Email());
				}
				if (COL_CONTACT_1_NAME.equals(field.getFieldName())) {					
					orderCols.add(line.getOhContact1Name());
				}
				if (COL_CONTACT_1_PHONE.equals(field.getFieldName())) {
					orderCols.add(line.getOhContact1Phone());					
				}
				if (COL_CONTACT_2_EMAIL.equals(field.getFieldName())) {
					orderCols.add(line.getOhContact2Email());					
				}
				if (COL_CONTACT_2_NAME.equals(field.getFieldName())) {
					orderCols.add(line.getOhContact2Name());					
				}
				if (COL_CONTACT_2_PHONE.equals(field.getFieldName())) {
					orderCols.add(line.getOhContact2Phone());					
				}
				if (COL_STATUS.equals(field.getFieldName())) {
					orderCols.add(line.getOhStatus());					
				}
				if (COL_JOINT_DELIVERY.equals(field.getFieldName())) {
					orderCols.add(line.getOhJointDelivery());					
				}
				if (COL_ORDER_DATE.equals(field.getFieldName())) {
					orderCols.add(line.getOhOrderDate());					
				}
				if (COL_TRANSFER_DATE.equals(field.getFieldName())) {
					orderCols.add(line.getOhTransferDate());					
				}
				if (COL_CUSTOM_FIELD_1.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField1());					
				}
				if (COL_CUSTOM_FIELD_2.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField2());					
				}
				if (COL_CUSTOM_FIELD_3.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField3());					
				}
				if (COL_CUSTOM_FIELD_4.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField4());					
				}
				if (COL_CUSTOM_FIELD_5.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField5());					
				}
				if (COL_CUSTOM_FIELD_6.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField6());					
				}
				if (COL_CUSTOM_FIELD_7.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField7());					
				}
				if (COL_CUSTOM_FIELD_8.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField8());
				}
				if (COL_CUSTOM_FIELD_9.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField9());					
				}
				if (COL_CUSTOM_FIELD_10.equals(field.getFieldName())) {
					orderCols.add(line.getOhCustomField10());					
				}
				
				
				if (COL_ARTICLE_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOlArticleNumber());
				}
				if (COL_ARTICLE_DESCRIPION.equals(field.getFieldName())) {
					orderCols.add(line.getOlArticleDescription());
				}
				if (COL_ROW_NUMBER.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlRowNumber()));
				}
				if (COL_CUSTUMER_ROW_NUMBER.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlCustomerRowNumber()));
				}								
				if (COL_INSTALLATION_TYPE.equals(field.getFieldName())) {
					orderCols.add(line.getOlInstallationType());
				}
				if (COL_OPERATING_SYSTEM.equals(field.getFieldName())) {
					orderCols.add(line.getOlOperatingSystem());
				}
				if (COL_OU.equals(field.getFieldName())) {
					orderCols.add(line.getOlOrganisationUnit());
				}
				if (COL_TOTAL.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlTotal()));
				}
				if (COL_REGISTERED.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlRegistered()));
				}
				if (COL_REMAINING.equals(field.getFieldName())) {
					orderCols.add(String.valueOf(line.getOlRemaining()));
				}
				if (COL_OL_LEASING_NUMBER.equals(field.getFieldName())) {
					orderCols.add(line.getOlLeasingNumber());
				}
				if (COL_RITM.equals(field.getFieldName())) {
					orderCols.add(line.getOlRITM());
				}
				
				if (COL_SERIAL_NO.equals(field.getFieldName())) {
					orderCols.add(line.getEqSerialNo());
				}
				if (COL_STEALING_TAG.equals(field.getFieldName())) {
					orderCols.add(line.getEqStealingTag());
				}
				if (COL_EQ_CUST_ATTRIBUTE_1.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute1());
				}
				if (COL_EQ_CUST_ATTRIBUTE_2.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute2());
				}
				if (COL_EQ_CUST_ATTRIBUTE_3.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute3());
				}
				if (COL_EQ_CUST_ATTRIBUTE_4.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute4());
				}
				if (COL_EQ_CUST_ATTRIBUTE_5.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute5());
				}
				if (COL_EQ_CUST_ATTRIBUTE_6.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute6());
				}
				if (COL_EQ_CUST_ATTRIBUTE_7.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute7());
				}
				if (COL_EQ_CUST_ATTRIBUTE_8.equals(field.getFieldName())) {
					orderCols.add(line.getEqCustomAttribute8());
				}
			}
			results.add(orderCols);
		}
		return results;
	}
		
	private DeliveryReportLine getReportLine(OrderHeader order, OrderLine orderLine, Equipment equipment) {
		DeliveryReportLine line = new DeliveryReportLine();

		line.setOhCity(order.getCity());
		line.setOhContact1Email(order.getContact1Email());
		line.setOhContact1Name(order.getContact1Name());
		line.setOhContact1Phone(order.getContact1Phone());
		line.setOhContact2Email(order.getContact2Email());
		line.setOhContact2Name(order.getContact2Name());
		line.setOhContact2Phone(order.getContact2Phone());
		line.setOhCustomerName(order.getCustomerName());
		line.setOhCustomerNumber(order.getCustomerNumber());
		line.setOhCustomerOrderNumber(order.getCustomerOrderNumber());
		line.setOhCustomerSalesOrder(order.getCustomerSalesOrder());
		
		List<String> customFieldValues = order.getAllCustomFieldValuesForReport();
		line.setOhCustomField1(customFieldValues.get(0));
		line.setOhCustomField2(customFieldValues.get(1));
		line.setOhCustomField3(customFieldValues.get(2));
		line.setOhCustomField4(customFieldValues.get(3));
		line.setOhCustomField5(customFieldValues.get(4));
		line.setOhCustomField6(customFieldValues.get(5));
		line.setOhCustomField7(customFieldValues.get(6));
		line.setOhCustomField8(customFieldValues.get(7));
		line.setOhCustomField9(customFieldValues.get(8));
		line.setOhCustomField10(customFieldValues.get(9));
		
		line.setOhDeliveryCity(order.getCity());
		line.setOhDeliveryAddress(order.getDeliveryAddressName());
		line.setOhDeliveryCity(order.getDeliveryCity());
		line.setOhDeliveryDate(order.getDeliveryDateDisplay());
		line.setOhDeliveryPostalAddress1(order.getDeliveryPostalAddress1());
		line.setOhDeliveryPostalAddress2(order.getDeliveryPostalAddress2());
		line.setOhDeliveryPostalCode(order.getDeliveryPostalCode());
		line.setOhJointDelivery(order.getJointDelivery());
		line.setOhLeasingNumber(order.getLeasingNumber());
		line.setOhNetsetOrderNumber(order.getNetsetOrderNumber());
		line.setOhOrderDate(order.getOrderDateAsString());
		line.setOhOrderNumber(order.getOrderNumber());
		line.setOhPostalAddress1(order.getPostalAddress1());
		line.setOhPostalAddress2(order.getPostalAddress2());
		line.setOhPostalCode(order.getPostalCode());
		line.setOhStatus(order.getStatusDisplay());
		line.setOhTransferDate(DateUtil.dateToString(order.getTransferDate()));

		line.setOlArticleDescription(orderLine.getArticleDescription());
		line.setOlArticleNumber(orderLine.getArticleNumber());
		line.setOlCustomerRowNumber(orderLine.getCustomerRowNumber());
		line.setOlInstallationType(orderLine.getInstallationType());
		line.setOlLeasingNumber(orderLine.getLeasingNumber());
		line.setOlOperatingSystem(orderLine.getOperatingSystem());
		line.setOlOrganisationUnit(orderLine.getOrganisationUnit());
		line.setOlRegistered(orderLine.getRegistered());
		line.setOlRemaining(orderLine.getRemaining());
		line.setOlRITM(orderLine.getRequestItemNumber());
		line.setOlRowNumber(orderLine.getRowNumber());
		line.setOlTotal(orderLine.getTotal());
		if (equipment != null) {
			line.setEqSerialNo(equipment.getSerialNo());
			line.setEqStealingTag(equipment.getStealingTag());
			line.setEqCustomAttribute1(equipment.getCustomAttribute1());
			line.setEqCustomAttribute2(equipment.getCustomAttribute2());
			line.setEqCustomAttribute3(equipment.getCustomAttribute3());
			line.setEqCustomAttribute4(equipment.getCustomAttribute4());
			line.setEqCustomAttribute5(equipment.getCustomAttribute5());
			line.setEqCustomAttribute6(equipment.getCustomAttribute6());
			line.setEqCustomAttribute7(equipment.getCustomAttribute7());
			line.setEqCustomAttribute8(equipment.getCustomAttribute8());
		}
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