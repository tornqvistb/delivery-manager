package se.lanteam.constants;

public class StatusConstants {

	public static final int PICK_STATUS_NOT_PICKED = 0;
	public static final int PICK_STATUS_PARTLY_PICKED = 1;
	public static final int PICK_STATUS_FULLY_PICKED = 2;
	
	public static final String ORDER_COMMENT_STATUS_NEW = "new";

	public static final String ORDER_STATUS_NOT_PICKED = "not_picked";
	public static final String ORDER_STATUS_BOOKED = "booked";
	public static final String ORDER_STATUS_STARTED = "started";
	public static final String ORDER_STATUS_REGISTRATION_DONE = "registration_done";
	public static final String ORDER_STATUS_ROUTE_PLANNED = "routeplanned";
	public static final String ORDER_STATUS_NOT_ACCEPTED = "not_accepted";
	public static final String ORDER_STATUS_SENT_CUSTOMER = "sent_customer";
	public static final String ORDER_STATUS_TRANSFERED_CUSTOMER = "transferred_customer";
	public static final String ORDER_STATUS_SENT = "sent";
	public static final String ORDER_STATUS_TRANSFERED = "transferred";
	public static final String ORDER_STATUS_DELIVERY_ERROR = "delivery_error";
	
	public static final String ORDER_STATUS_GROUP_ACTIVE = "active";
	public static final String ORDER_STATUS_GROUP_INACTIVE = "inactive";
	public static final String ORDER_STATUS_GROUP_ALL = "all";
	
	public static final String ORDER_STATUS_NOT_PICKED_DISP = "Ej plockad";
	public static final String ORDER_STATUS_NEW_DISP = "Ny";
	public static final String ORDER_STATUS_BOOKED_DISP = "Bokad";
	public static final String ORDER_STATUS_STARTED_DISP = "Påbörjad";
	public static final String ORDER_STATUS_REGISTRATION_DONE_DISP = "Registrering klar";
	public static final String ORDER_STATUS_ROUTE_PLANNED_DISP = "Ruttplanerad";
	public static final String ORDER_STATUS_NOT_ACCEPTED_DISP = "Ej accepterad";
	public static final String ORDER_STATUS_SENT_CUSTOMER_DISP = "Leveransavisering skickad";
	public static final String ORDER_STATUS_TRANSFERED_CUSTOMER_DISP = "Överförd - inväntar leveranssedel";
	public static final String ORDER_STATUS_SENT_DISP = "Skickad";
	public static final String ORDER_STATUS_TRANSFERED_DISP = "Överförd till kund";
	public static final String ORDER_STATUS_DELIVERY_ERROR_DISP = "Fel vid leverans";
	
	
	public static final String[][] ORDER_STATUS_MATRIX = {{ORDER_STATUS_NOT_PICKED, ORDER_STATUS_NOT_PICKED_DISP},
														  {ORDER_STATUS_BOOKED, ORDER_STATUS_BOOKED_DISP},
														  {ORDER_STATUS_STARTED, ORDER_STATUS_STARTED_DISP},
														  {ORDER_STATUS_REGISTRATION_DONE, ORDER_STATUS_REGISTRATION_DONE_DISP},
														  {ORDER_STATUS_ROUTE_PLANNED, ORDER_STATUS_ROUTE_PLANNED_DISP},
														  {ORDER_STATUS_NOT_ACCEPTED, ORDER_STATUS_NOT_ACCEPTED_DISP},
														  {ORDER_STATUS_SENT_CUSTOMER, ORDER_STATUS_SENT_CUSTOMER_DISP},
														  {ORDER_STATUS_TRANSFERED_CUSTOMER, ORDER_STATUS_TRANSFERED_CUSTOMER_DISP},
														  {ORDER_STATUS_SENT, ORDER_STATUS_SENT_DISP},
														  {ORDER_STATUS_TRANSFERED, ORDER_STATUS_TRANSFERED_DISP},
														  {ORDER_STATUS_DELIVERY_ERROR, ORDER_STATUS_DELIVERY_ERROR_DISP},
														  };
	
	public static final String[] ACTIVE_STATI = {ORDER_STATUS_NOT_PICKED, ORDER_STATUS_BOOKED, ORDER_STATUS_STARTED, ORDER_STATUS_REGISTRATION_DONE, ORDER_STATUS_ROUTE_PLANNED, ORDER_STATUS_NOT_ACCEPTED, ORDER_STATUS_DELIVERY_ERROR};
	public static final String[] INACTIVE_STATI = {ORDER_STATUS_SENT_CUSTOMER, ORDER_STATUS_TRANSFERED_CUSTOMER, ORDER_STATUS_SENT, ORDER_STATUS_TRANSFERED};
	public static final String[] ALL_STATI = StatusUtil.concatenate(ACTIVE_STATI, INACTIVE_STATI); 

	public static final String ERROR_STATUS_NEW = "new";
	public static final String ERROR_STATUS_ARCHIVED = "archived";

	public static final String EMAIL_STATUS_NEW = "new";
	public static final String EMAIL_STATUS_SENT = "sent";
	
}
