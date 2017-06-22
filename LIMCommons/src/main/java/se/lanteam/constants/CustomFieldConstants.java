package se.lanteam.constants;

public class CustomFieldConstants {
	public static final long CUSTOM_FIELD_CONTACT_NAME = 1;
	public static final long CUSTOM_FIELD_CONTACT_EMAIL = 2;
	public static final long CUSTOM_FIELD_CONTACT_PHONE = 3;
    
	public static final long[] CONTACT_FIELDS = {CUSTOM_FIELD_CONTACT_NAME, CUSTOM_FIELD_CONTACT_EMAIL, CUSTOM_FIELD_CONTACT_PHONE};
	
	public static final long CUSTOM_FIELD_JOINT_DELIVERY = 9;
	public static final String VALUE_SAMLEVERANS_MASTER = "Ja";	
	public static final String TEXT_SAMLEVERANS_MASTER = "OBS!!! Det finns en underorder med ordernr %s till denna order.";
	public static final String TEXT_SAMLEVERANS_CHILD = "OBS!!! Det finns en huvudorder med ordernr %s till denna order.";

}
