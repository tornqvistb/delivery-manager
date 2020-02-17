package se.lanteam.model.comparator;

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

import java.util.Comparator;

import se.lanteam.model.DeliveryReportLine;

public class DeliveryReportSorter implements Comparator<DeliveryReportLine> 
{ 
	
	private String sortByColumn;
	
    public DeliveryReportSorter(String sortByColumn) {
		super();
		this.sortByColumn = sortByColumn;
	}
	public int compare(DeliveryReportLine a, DeliveryReportLine b) 
    { 
		switch(sortByColumn) 
        { 
	        case COL_ORDER_NUMBER:
	            return a.getOhOrderNumber().compareTo(b.getOhOrderNumber()); 
	    	case COL_CUSTOMER_ORDER_NUMBER:
	            return a.getOhCustomerOrderNumber().compareTo(b.getOhCustomerOrderNumber()); 
	    	case COL_CUSTOMER_SALES_ORDER:	
	            return a.getOhCustomerSalesOrder().compareTo(b.getOhCustomerSalesOrder()); 
	    	case COL_NETSET_ORDER_NUMBER:
	            return a.getOhNetsetOrderNumber().compareTo(b.getOhNetsetOrderNumber()); 
	    	case COL_OH_LEASING_NUMBER:
	            return a.getOhLeasingNumber().compareTo(b.getOhLeasingNumber()); 	    		
	    	case COL_CUSTOMER_NAME:
	            return a.getOhCustomerName().compareTo(b.getOhCustomerName()); 
	    	case COL_CUSTOMER_NUMBER:
	            return a.getOhCustomerNumber().compareTo(b.getOhCustomerNumber()); 
	    	case COL_DELIVERY_ADDRESS:
	            return a.getOhDeliveryAddress().compareTo(b.getOhDeliveryAddress()); 
	    	case COL_DELIVERY_CITY:
	            return a.getOhDeliveryCity().compareTo(b.getOhDeliveryCity()); 
	    	case COL_DELIVERY_DATE:
	            return a.getOhDeliveryDate().compareTo(b.getOhDeliveryDate()); 
	    	case COL_DELIVERY_POSTAL_ADDRESS_1:
	            return a.getOhDeliveryPostalAddress1().compareTo(b.getOhDeliveryPostalAddress1()); 
	    	case COL_DELIVERY_POSTAL_ADDRESS_2:
	            return a.getOhDeliveryPostalAddress2().compareTo(b.getOhDeliveryPostalAddress2()); 
	    	case COL_DELIVERY_POSTAL_CODE:
	            return a.getOhDeliveryPostalCode().compareTo(b.getOhDeliveryPostalCode()); 
	    	case COL_CITY:
	            return a.getOhCity().compareTo(b.getOhCity()); 
	    	case COL_POSTAL_ADDRESS_1:
	            return a.getOhPostalAddress1().compareTo(b.getOhPostalAddress1()); 
	    	case COL_POSTAL_ADDRESS_2:
	            return a.getOhPostalAddress2().compareTo(b.getOhPostalAddress2()); 
	    	case COL_POSTAL_CODE:
	            return a.getOhPostalCode().compareTo(b.getOhPostalCode()); 
	    	case COL_CONTACT_1_EMAIL:
	            return a.getOhContact1Email().compareTo(b.getOhContact1Email()); 
	    	case COL_CONTACT_1_NAME:
	            return a.getOhContact1Name().compareTo(b.getOhContact1Name()); 
	    	case COL_CONTACT_1_PHONE:
	            return a.getOhContact1Phone().compareTo(b.getOhContact1Phone()); 
	    	case COL_CONTACT_2_EMAIL:
	            return a.getOhContact2Email().compareTo(b.getOhContact2Email()); 
	    	case COL_CONTACT_2_NAME:
	            return a.getOhContact2Name().compareTo(b.getOhContact2Name()); 
	    	case COL_CONTACT_2_PHONE:
	            return a.getOhContact2Phone().compareTo(b.getOhContact2Phone()); 
	    	case COL_STATUS:
	            return a.getOhStatus().compareTo(b.getOhStatus()); 
	    	case COL_JOINT_DELIVERY:
	            return a.getOhJointDelivery().compareTo(b.getOhJointDelivery()); 
	    	case COL_ORDER_DATE:
	            return a.getOhOrderDate().compareTo(b.getOhOrderDate()); 
	    	case COL_TRANSFER_DATE:
	            return a.getOhTransferDate().compareTo(b.getOhTransferDate()); 
	    	case COL_CUSTOM_FIELD_1:
	            return a.getOhCustomField1().compareTo(b.getOhCustomField1()); 
	    	case COL_CUSTOM_FIELD_2:
	            return a.getOhCustomField2().compareTo(b.getOhCustomField2()); 
	    	case COL_CUSTOM_FIELD_3:
	            return a.getOhCustomField3().compareTo(b.getOhCustomField3()); 
	    	case COL_CUSTOM_FIELD_4:
	            return a.getOhCustomField4().compareTo(b.getOhCustomField4()); 
	    	case COL_CUSTOM_FIELD_5:
	            return a.getOhCustomField5().compareTo(b.getOhCustomField5()); 
	    	case COL_CUSTOM_FIELD_6:
	            return a.getOhCustomField6().compareTo(b.getOhCustomField6()); 
	    	case COL_CUSTOM_FIELD_7:
	            return a.getOhCustomField7().compareTo(b.getOhCustomField7()); 
	    	case COL_CUSTOM_FIELD_8:
	            return a.getOhCustomField8().compareTo(b.getOhCustomField8()); 
	    	case COL_CUSTOM_FIELD_9:
	            return a.getOhCustomField9().compareTo(b.getOhCustomField9()); 
	    	case COL_CUSTOM_FIELD_10:			
	            return a.getOhCustomField10().compareTo(b.getOhCustomField10()); 
	    	case COL_ARTICLE_NUMBER:
	            return a.getOlArticleNumber().compareTo(b.getOlArticleNumber()); 
	    	case COL_ARTICLE_DESCRIPION:
	            return a.getOlArticleDescription().compareTo(b.getOlArticleDescription()); 
	    	case COL_ROW_NUMBER:	
	            return a.getOlRowNumber() - b.getOlRowNumber(); 
	    	case COL_CUSTUMER_ROW_NUMBER:
	    		return a.getOlCustomerRowNumber() - b.getOlCustomerRowNumber(); 
	    	case COL_INSTALLATION_TYPE:
	            return a.getOlInstallationType().compareTo(b.getOlInstallationType()); 
	    	case COL_OPERATING_SYSTEM:
	            return a.getOlInstallationType().compareTo(b.getOlInstallationType()); 
	    	case COL_OU:
	            return a.getOlOrganisationUnit().compareTo(b.getOlOrganisationUnit()); 
	    	case COL_REGISTERED:
	    		return a.getOlRegistered() - b.getOlRegistered(); 
	    	case COL_REMAINING:
	    		return a.getOlRemaining() - b.getOlRemaining(); 
	    	case COL_TOTAL:
	    		return a.getOlTotal() - b.getOlTotal(); 
	    	case COL_OL_LEASING_NUMBER:
	            return a.getOlLeasingNumber().compareTo(b.getOlLeasingNumber()); 
	    	case COL_RITM:				
	            return a.getOlRITM().compareTo(b.getOlRITM()); 
	    	case COL_SERIAL_NO:
	            return a.getEqSerialNo().compareTo(b.getEqSerialNo()); 
	    	case COL_STEALING_TAG:
	            return a.getEqStealingTag().compareTo(b.getEqStealingTag()); 
	    	case COL_EQ_CUST_ATTRIBUTE_1:
	            return a.getEqCustomAttribute1().compareTo(b.getEqCustomAttribute1()); 
	    	case COL_EQ_CUST_ATTRIBUTE_2:
	            return a.getEqCustomAttribute2().compareTo(b.getEqCustomAttribute2()); 
	    	case COL_EQ_CUST_ATTRIBUTE_3:
	            return a.getEqCustomAttribute3().compareTo(b.getEqCustomAttribute3()); 
	    	case COL_EQ_CUST_ATTRIBUTE_4:
	            return a.getEqCustomAttribute4().compareTo(b.getEqCustomAttribute4()); 
	    	case COL_EQ_CUST_ATTRIBUTE_5:
	            return a.getEqCustomAttribute5().compareTo(b.getEqCustomAttribute5()); 
	    	case COL_EQ_CUST_ATTRIBUTE_6:
	            return a.getEqCustomAttribute6().compareTo(b.getEqCustomAttribute6()); 
	    	case COL_EQ_CUST_ATTRIBUTE_7:
	            return a.getEqCustomAttribute7().compareTo(b.getEqCustomAttribute7()); 
	    	case COL_EQ_CUST_ATTRIBUTE_8:
	            return a.getEqCustomAttribute8().compareTo(b.getEqCustomAttribute8()); 
            default: 
	            return a.getOhOrderNumber().compareTo(b.getOhOrderNumber()); 
        } 
    } 
    
    
    
    
}