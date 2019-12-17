package se.lanteam.model;

public class DeliveryReportLine {

	public static final String COL_ORDER_NUMBER = "ohOrderNumber";
	public static final String COL_CUSTOMER_ORDER_NUMBER = "ohCustomerOrderNumber";
	public static final String COL_CUSTOMER_SALES_ORDER = "ohCustomerSalesOrder";	
	public static final String COL_NETSET_ORDER_NUMBER = "ohNetsetOrderNumber";
	public static final String COL_OH_LEASING_NUMBER = "ohLeasingNumber";
	public static final String COL_CUSTOMER_NAME = "ohCustomerName";
	public static final String COL_CUSTOMER_NUMBER = "ohCustomerNumber";
	public static final String COL_DELIVERY_ADDRESS = "ohDeliveryAddress";
	public static final String COL_DELIVERY_CITY = "ohDeliveryCity";
	public static final String COL_DELIVERY_DATE = "ohDeliveryDate";
	public static final String COL_DELIVERY_POSTAL_ADDRESS_1 = "ohDeliveryPostalAddress1";
	public static final String COL_DELIVERY_POSTAL_ADDRESS_2 = "ohDeliveryPostalAddress2";
	public static final String COL_DELIVERY_POSTAL_CODE = "ohDeliveryPostalCode";
	public static final String COL_CITY = "ohCity";
	public static final String COL_POSTAL_ADDRESS_1 = "ohPostalAddress1";
	public static final String COL_POSTAL_ADDRESS_2 = "ohPostalAddress2";
	public static final String COL_POSTAL_CODE = "ohPostalCode";
	public static final String COL_CONTACT_1_EMAIL = "ohContact1Email";
	public static final String COL_CONTACT_1_NAME = "ohContact1Name";
	public static final String COL_CONTACT_1_PHONE = "ohContact1Phone";
	public static final String COL_CONTACT_2_EMAIL = "ohContact2Email";
	public static final String COL_CONTACT_2_NAME = "ohContact2Name";
	public static final String COL_CONTACT_2_PHONE = "ohContact2Phone";
	public static final String COL_STATUS = "ohStatus";
	public static final String COL_JOINT_DELIVERY = "ohJointDelivery";
	public static final String COL_ORDER_DATE = "ohOrderDate";
	public static final String COL_TRANSFER_DATE = "ohTransferDate";
	public static final String COL_CUSTOM_FIELD_1 = "ohCustomField1";
	public static final String COL_CUSTOM_FIELD_2 = "ohCustomField2";
	public static final String COL_CUSTOM_FIELD_3 = "ohCustomField3";
	public static final String COL_CUSTOM_FIELD_4 = "ohCustomField4";
	public static final String COL_CUSTOM_FIELD_5 = "ohCustomField5";
	public static final String COL_CUSTOM_FIELD_6 = "ohCustomField6";
	public static final String COL_CUSTOM_FIELD_7 = "ohCustomField7";
	public static final String COL_CUSTOM_FIELD_8 = "ohCustomField8";
	public static final String COL_CUSTOM_FIELD_9 = "ohCustomField9";
	public static final String COL_CUSTOM_FIELD_10 = "ohCustomField10";
			
	public static final String COL_ARTICLE_NUMBER = "olArticleNumber";
	public static final String COL_ARTICLE_DESCRIPION = "olArticleDescription";
	public static final String COL_ROW_NUMBER = "olRowNumber";	
	public static final String COL_CUSTUMER_ROW_NUMBER = "olCustomerRowNumber";
	public static final String COL_INSTALLATION_TYPE = "olInstallationType";
	public static final String COL_OPERATING_SYSTEM = "olOperatingSystem";
	public static final String COL_OU = "olOrganisationUnit";
	public static final String COL_REGISTERED = "olRegistered";
	public static final String COL_REMAINING = "olRemaining";
	public static final String COL_TOTAL = "olTotal";
	public static final String COL_OL_LEASING_NUMBER = "olLeasingNumber";
	public static final String COL_RITM = "olRITM";	
			
	public static final String COL_SERIAL_NO = "eqSerialNo";
	public static final String COL_STEALING_TAG = "eqStealingTag";
	public static final String COL_EQ_CUST_ATTRIBUTE_1 = "eqCustomAttribute1";
	public static final String COL_EQ_CUST_ATTRIBUTE_2 = "eqCustomAttribute2";
	public static final String COL_EQ_CUST_ATTRIBUTE_3 = "eqCustomAttribute3";
	public static final String COL_EQ_CUST_ATTRIBUTE_4 = "eqCustomAttribute4";
	public static final String COL_EQ_CUST_ATTRIBUTE_5 = "eqCustomAttribute5";
	public static final String COL_EQ_CUST_ATTRIBUTE_6 = "eqCustomAttribute6";
	public static final String COL_EQ_CUST_ATTRIBUTE_7 = "eqCustomAttribute7";
	public static final String COL_EQ_CUST_ATTRIBUTE_8 = "eqCustomAttribute8";
	
	private String ohOrderNumber = "";
	private String ohCustomerOrderNumber = "";
	private String ohCustomerSalesOrder = "";	
	private String ohNetsetOrderNumber = "";
	private String ohLeasingNumber = "";
	private String ohCustomerName = "";
	private String ohCustomerNumber = "";
	private String ohDeliveryAddress = "";
	private String ohDeliveryCity = "";
	private String ohDeliveryDate = "";
	private String ohDeliveryPostalAddress1 = "";
	private String ohDeliveryPostalAddress2 = "";
	private String ohDeliveryPostalCode = "";
	private String ohCity = "";
	private String ohPostalAddress1 = "";
	private String ohPostalAddress2 = "";
	private String ohPostalCode = "";
	private String ohContact1Email = "";
	private String ohContact1Name = "";
	private String ohContact1Phone = "";
	private String ohContact2Email = "";
	private String ohContact2Name = "";
	private String ohContact2Phone = "";
	private String ohStatus = "";
	private String ohJointDelivery = "";
	private String ohOrderDate = "";
	private String ohTransferDate = "";
	private String ohCustomField1 = "";
	private String ohCustomField2 = "";
	private String ohCustomField3 = "";
	private String ohCustomField4 = "";
	private String ohCustomField5 = "";
	private String ohCustomField6 = "";
	private String ohCustomField7 = "";
	private String ohCustomField8 = "";
	private String ohCustomField9 = "";
	private String ohCustomField10 = "";
	private String olArticleNumber = "";
	private String olArticleDescription = "";
	private int olRowNumber = 0;	
	private int olCustomerRowNumber = 0;
	private String olInstallationType = "";
	private String olOperatingSystem = "";
	private String olOrganisationUnit = "";
	private int olRegistered = 0;
	private int olRemaining = 0;
	private int olTotal = 0;
	private String olLeasingNumber = "";
	private String olRITM = "";	
	private String eqSerialNo = "";
	private String eqStealingTag = "";
	private String eqCustomAttribute1 = "";
	private String eqCustomAttribute2 = "";
	private String eqCustomAttribute3 = "";
	private String eqCustomAttribute4 = "";
	private String eqCustomAttribute5 = "";
	private String eqCustomAttribute6 = "";
	private String eqCustomAttribute7 = "";
	private String eqCustomAttribute8 = "";
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
	public String getOhNetsetOrderNumber() {
		return ohNetsetOrderNumber;
	}
	public void setOhNetsetOrderNumber(String ohNetsetOrderNumber) {
		this.ohNetsetOrderNumber = ohNetsetOrderNumber;
	}
	public String getOhLeasingNumber() {
		return ohLeasingNumber;
	}
	public void setOhLeasingNumber(String ohLeasingNumber) {
		this.ohLeasingNumber = ohLeasingNumber;
	}
	public String getOhCustomerName() {
		return ohCustomerName;
	}
	public void setOhCustomerName(String ohCustomerName) {
		this.ohCustomerName = ohCustomerName;
	}
	public String getOhCustomerNumber() {
		return ohCustomerNumber;
	}
	public void setOhCustomerNumber(String ohCustomerNumber) {
		this.ohCustomerNumber = ohCustomerNumber;
	}
	public String getOhDeliveryAddress() {
		return ohDeliveryAddress;
	}
	public void setOhDeliveryAddress(String ohDeliveryAddress) {
		this.ohDeliveryAddress = ohDeliveryAddress;
	}
	public String getOhDeliveryCity() {
		return ohDeliveryCity;
	}
	public void setOhDeliveryCity(String ohDeliveryCity) {
		this.ohDeliveryCity = ohDeliveryCity;
	}
	public String getOhDeliveryDate() {
		return ohDeliveryDate;
	}
	public void setOhDeliveryDate(String ohDeliveryDate) {
		this.ohDeliveryDate = ohDeliveryDate;
	}
	public String getOhDeliveryPostalAddress1() {
		return ohDeliveryPostalAddress1;
	}
	public void setOhDeliveryPostalAddress1(String ohDeliveryPostalAddress1) {
		this.ohDeliveryPostalAddress1 = ohDeliveryPostalAddress1;
	}
	public String getOhDeliveryPostalAddress2() {
		return ohDeliveryPostalAddress2;
	}
	public void setOhDeliveryPostalAddress2(String ohDeliveryPostalAddress2) {
		this.ohDeliveryPostalAddress2 = ohDeliveryPostalAddress2;
	}
	public String getOhDeliveryPostalCode() {
		return ohDeliveryPostalCode;
	}
	public void setOhDeliveryPostalCode(String ohDeliveryPostalCode) {
		this.ohDeliveryPostalCode = ohDeliveryPostalCode;
	}
	public String getOhCity() {
		return ohCity;
	}
	public void setOhCity(String ohCity) {
		this.ohCity = ohCity;
	}
	public String getOhPostalAddress1() {
		return ohPostalAddress1;
	}
	public void setOhPostalAddress1(String ohPostalAddress1) {
		this.ohPostalAddress1 = ohPostalAddress1;
	}
	public String getOhPostalAddress2() {
		return ohPostalAddress2;
	}
	public void setOhPostalAddress2(String ohPostalAddress2) {
		this.ohPostalAddress2 = ohPostalAddress2;
	}
	public String getOhPostalCode() {
		return ohPostalCode;
	}
	public void setOhPostalCode(String ohPostalCode) {
		this.ohPostalCode = ohPostalCode;
	}
	public String getOhContact1Email() {
		return ohContact1Email;
	}
	public void setOhContact1Email(String ohContact1Email) {
		this.ohContact1Email = ohContact1Email;
	}
	public String getOhContact1Name() {
		return ohContact1Name;
	}
	public void setOhContact1Name(String ohContact1Name) {
		this.ohContact1Name = ohContact1Name;
	}
	public String getOhContact1Phone() {
		return ohContact1Phone;
	}
	public void setOhContact1Phone(String ohContact1Phone) {
		this.ohContact1Phone = ohContact1Phone;
	}
	public String getOhContact2Email() {
		return ohContact2Email;
	}
	public void setOhContact2Email(String ohContact2Email) {
		this.ohContact2Email = ohContact2Email;
	}
	public String getOhContact2Name() {
		return ohContact2Name;
	}
	public void setOhContact2Name(String ohContact2Name) {
		this.ohContact2Name = ohContact2Name;
	}
	public String getOhContact2Phone() {
		return ohContact2Phone;
	}
	public void setOhContact2Phone(String ohContact2Phone) {
		this.ohContact2Phone = ohContact2Phone;
	}
	public String getOhStatus() {
		return ohStatus;
	}
	public void setOhStatus(String ohStatus) {
		this.ohStatus = ohStatus;
	}
	public String getOhJointDelivery() {
		return ohJointDelivery;
	}
	public void setOhJointDelivery(String ohJointDelivery) {
		this.ohJointDelivery = ohJointDelivery;
	}
	public String getOhOrderDate() {
		return ohOrderDate;
	}
	public void setOhOrderDate(String ohOrderDate) {
		this.ohOrderDate = ohOrderDate;
	}
	public String getOhTransferDate() {
		return ohTransferDate;
	}
	public void setOhTransferDate(String ohTransferDate) {
		this.ohTransferDate = ohTransferDate;
	}
	public String getOhCustomField1() {
		return ohCustomField1;
	}
	public void setOhCustomField1(String ohCustomField1) {
		this.ohCustomField1 = ohCustomField1;
	}
	public String getOhCustomField2() {
		return ohCustomField2;
	}
	public void setOhCustomField2(String ohCustomField2) {
		this.ohCustomField2 = ohCustomField2;
	}
	public String getOhCustomField3() {
		return ohCustomField3;
	}
	public void setOhCustomField3(String ohCustomField3) {
		this.ohCustomField3 = ohCustomField3;
	}
	public String getOhCustomField4() {
		return ohCustomField4;
	}
	public void setOhCustomField4(String ohCustomField4) {
		this.ohCustomField4 = ohCustomField4;
	}
	public String getOhCustomField5() {
		return ohCustomField5;
	}
	public void setOhCustomField5(String ohCustomField5) {
		this.ohCustomField5 = ohCustomField5;
	}
	public String getOhCustomField6() {
		return ohCustomField6;
	}
	public void setOhCustomField6(String ohCustomField6) {
		this.ohCustomField6 = ohCustomField6;
	}
	public String getOhCustomField7() {
		return ohCustomField7;
	}
	public void setOhCustomField7(String ohCustomField7) {
		this.ohCustomField7 = ohCustomField7;
	}
	public String getOhCustomField8() {
		return ohCustomField8;
	}
	public void setOhCustomField8(String ohCustomField8) {
		this.ohCustomField8 = ohCustomField8;
	}
	public String getOhCustomField9() {
		return ohCustomField9;
	}
	public void setOhCustomField9(String ohCustomField9) {
		this.ohCustomField9 = ohCustomField9;
	}
	public String getOhCustomField10() {
		return ohCustomField10;
	}
	public void setOhCustomField10(String ohCustomField10) {
		this.ohCustomField10 = ohCustomField10;
	}
	public String getOlArticleNumber() {
		return olArticleNumber;
	}
	public void setOlArticleNumber(String olArticleNumber) {
		this.olArticleNumber = olArticleNumber;
	}
	public String getOlArticleDescription() {
		return olArticleDescription;
	}
	public void setOlArticleDescription(String olArticleDescription) {
		this.olArticleDescription = olArticleDescription;
	}
	public int getOlRowNumber() {
		return olRowNumber;
	}
	public void setOlRowNumber(int olRowNumber) {
		this.olRowNumber = olRowNumber;
	}
	public int getOlCustomerRowNumber() {
		return olCustomerRowNumber;
	}
	public void setOlCustomerRowNumber(int olCustomerRowNumber) {
		this.olCustomerRowNumber = olCustomerRowNumber;
	}
	public String getOlInstallationType() {
		return olInstallationType;
	}
	public void setOlInstallationType(String olInstallationType) {
		this.olInstallationType = olInstallationType;
	}
	public String getOlOperatingSystem() {
		return olOperatingSystem;
	}
	public void setOlOperatingSystem(String olOperatingSystem) {
		this.olOperatingSystem = olOperatingSystem;
	}
	public String getOlOrganisationUnit() {
		return olOrganisationUnit;
	}
	public void setOlOrganisationUnit(String olOrganisationUnit) {
		this.olOrganisationUnit = olOrganisationUnit;
	}
	public int getOlRegistered() {
		return olRegistered;
	}
	public void setOlRegistered(int olRegistered) {
		this.olRegistered = olRegistered;
	}
	public int getOlRemaining() {
		return olRemaining;
	}
	public void setOlRemaining(int olRemaining) {
		this.olRemaining = olRemaining;
	}
	public int getOlTotal() {
		return olTotal;
	}
	public void setOlTotal(int olTotal) {
		this.olTotal = olTotal;
	}
	public String getOlLeasingNumber() {
		return olLeasingNumber;
	}
	public void setOlLeasingNumber(String olLeasingNumber) {
		this.olLeasingNumber = olLeasingNumber;
	}
	public String getOlRITM() {
		return olRITM;
	}
	public void setOlRITM(String olRITM) {
		this.olRITM = olRITM;
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
	public String getEqCustomAttribute1() {
		return eqCustomAttribute1;
	}
	public void setEqCustomAttribute1(String eqCustomAttribute1) {
		this.eqCustomAttribute1 = eqCustomAttribute1;
	}
	public String getEqCustomAttribute2() {
		return eqCustomAttribute2;
	}
	public void setEqCustomAttribute2(String eqCustomAttribute2) {
		this.eqCustomAttribute2 = eqCustomAttribute2;
	}
	public String getEqCustomAttribute3() {
		return eqCustomAttribute3;
	}
	public void setEqCustomAttribute3(String eqCustomAttribute3) {
		this.eqCustomAttribute3 = eqCustomAttribute3;
	}
	public String getEqCustomAttribute4() {
		return eqCustomAttribute4;
	}
	public void setEqCustomAttribute4(String eqCustomAttribute4) {
		this.eqCustomAttribute4 = eqCustomAttribute4;
	}
	public String getEqCustomAttribute5() {
		return eqCustomAttribute5;
	}
	public void setEqCustomAttribute5(String eqCustomAttribute5) {
		this.eqCustomAttribute5 = eqCustomAttribute5;
	}
	public String getEqCustomAttribute6() {
		return eqCustomAttribute6;
	}
	public void setEqCustomAttribute6(String eqCustomAttribute6) {
		this.eqCustomAttribute6 = eqCustomAttribute6;
	}
	public String getEqCustomAttribute7() {
		return eqCustomAttribute7;
	}
	public void setEqCustomAttribute7(String eqCustomAttribute7) {
		this.eqCustomAttribute7 = eqCustomAttribute7;
	}
	public String getEqCustomAttribute8() {
		return eqCustomAttribute8;
	}
	public void setEqCustomAttribute8(String eqCustomAttribute8) {
		this.eqCustomAttribute8 = eqCustomAttribute8;
	}

	
}
