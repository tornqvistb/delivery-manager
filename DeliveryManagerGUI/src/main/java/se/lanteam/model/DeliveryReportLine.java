package se.lanteam.model;

import java.util.Comparator;

public class DeliveryReportLine {

	public static final String COL_ORDER_NUMBER = "ohOrderNumber";
	public static final String COL_CUSTOMER_ORDER_NUMBER = "ohCustomerOrderNumber";
	public static final String COL_CUSTOMER_SALES_ORDER = "ohCustomerSalesOrder";
	public static final String COL_ARTICLE_NUMBER = "olArticleNumber";
	public static final String COL_CUST_ROW_NUMBER = "olCustomerRowNumber";
	public static final String COL_TOTAL = "olTotal";
	public static final String COL_SERIAL_NO = "eqSerialNo";
	public static final String COL_STEALING_TAG = "eqStealingTag";
	public static final String COL_EQ_CUST_LABEL_1 = "eqCustomLabel1";
	
	private String ohOrderNumber;
	private String ohCustomerOrderNumber;
	private String ohCustomerSalesOrder;
	private String olArticleNumber;
	private int olCustomerRowNumber;
	private int olTotal;
	private String eqSerialNo;
	private String eqStealingTag;
	private String eqCustomLabel1;
	
	public String getOhOrderNumber() {
		return ohOrderNumber;
	}
	public void setOhOrderNumber(String ohOrderNumber) {
		this.ohOrderNumber = ohOrderNumber;
	}
	public String getOhCustomerOrderNumber() {
		return ohCustomerOrderNumber;
	}
	public void setOhCustomerOrderNumber(String ohCustomerOrderNumber) {
		this.ohCustomerOrderNumber = ohCustomerOrderNumber;
	}
	public String getOhCustomerSalesOrder() {
		return ohCustomerSalesOrder;
	}
	public void setOhCustomerSalesOrder(String ohCustomerSalesOrder) {
		this.ohCustomerSalesOrder = ohCustomerSalesOrder;
	}
	public String getOlArticleNumber() {
		return olArticleNumber;
	}
	public void setOlArticleNumber(String olArticleNumber) {
		this.olArticleNumber = olArticleNumber;
	}
	public int getOlCustomerRowNumber() {
		return olCustomerRowNumber;
	}
	public void setOlCustomerRowNumber(int olCustomerRowNumber) {
		this.olCustomerRowNumber = olCustomerRowNumber;
	}
	public int getOlTotal() {
		return olTotal;
	}
	public void setOlTotal(int olTotal) {
		this.olTotal = olTotal;
	}
	public String getEqSerialNo() {
		return eqSerialNo;
	}
	public void setEqSerialNo(String eqSerialNo) {
		this.eqSerialNo = eqSerialNo;
	}
	public String getEqStealingTag() {
		return eqStealingTag;
	}
	public void setEqStealingTag(String eqStealingTag) {
		this.eqStealingTag = eqStealingTag;
	}
	public String getEqCustomLabel1() {
		return eqCustomLabel1;
	}
	public void setEqCustomLabel1(String eqCustomLabel1) {
		this.eqCustomLabel1 = eqCustomLabel1;
	}
	
	class SortbyohOrderNumber implements Comparator<DeliveryReportLine> 
	{ 
	    public int compare(DeliveryReportLine a, DeliveryReportLine b) 
	    { 
	        return a.ohOrderNumber.compareTo(b.ohOrderNumber); 
	    } 
	} 
	
}
