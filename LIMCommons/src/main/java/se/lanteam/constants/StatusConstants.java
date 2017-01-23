package se.lanteam.constants;

public class StatusConstants {

	public static final String ORDER_STATUS_NEW = "new";
	public static final String ORDER_STATUS_STARTED = "started";
	public static final String ORDER_STATUS_REGISTRATION_DONE = "registration_done";
	public static final String ORDER_STATUS_NOT_ACCEPTED = "not_accepted";
	public static final String ORDER_STATUS_SENT = "sent";
	public static final String ORDER_STATUS_TRANSFERED = "transferred";
	
	public static final String ORDER_STATUS_GROUP_ACTIVE = "active";
	public static final String ORDER_STATUS_GROUP_INACTIVE = "inactive";
	
	public static final String ORDER_STATUS_NEW_DISP = "Ny";
	public static final String ORDER_STATUS_STARTED_DISP = "Påbörjad";
	public static final String ORDER_STATUS_REGISTRATION_DONE_DISP = "Registrering klar";
	public static final String ORDER_STATUS_NOT_ACCEPTED_DISP = "Ej accepterad";
	public static final String ORDER_STATUS_SENT_DISP = "Skickad";
	public static final String ORDER_STATUS_TRANSFERED_DISP = "Överförd till Intraservice";
	
	
	public static final String[][] ORDER_STATUS_MATRIX = {{ORDER_STATUS_NEW, ORDER_STATUS_NEW_DISP},
														  {ORDER_STATUS_STARTED, ORDER_STATUS_STARTED_DISP},
														  {ORDER_STATUS_REGISTRATION_DONE, ORDER_STATUS_REGISTRATION_DONE_DISP},
														  {ORDER_STATUS_NOT_ACCEPTED, ORDER_STATUS_NOT_ACCEPTED_DISP},
														  {ORDER_STATUS_SENT, ORDER_STATUS_SENT_DISP},
														  {ORDER_STATUS_TRANSFERED, ORDER_STATUS_TRANSFERED_DISP},
														  };
	
	public static final String[] ACTIVE_STATI = {ORDER_STATUS_NEW, ORDER_STATUS_STARTED, ORDER_STATUS_REGISTRATION_DONE, ORDER_STATUS_NOT_ACCEPTED};
	public static final String[] INACTIVE_STATI = {ORDER_STATUS_SENT, ORDER_STATUS_TRANSFERED};	

	public static final String ERROR_STATUS_NEW = "new";
	public static final String ERROR_STATUS_ARCHIVED = "archived";

	public static final String EMAIL_STATUS_NEW = "new";
	public static final String EMAIL_STATUS_SENT = "sent";
	
}
