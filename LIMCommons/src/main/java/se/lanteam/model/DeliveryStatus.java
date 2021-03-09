package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

public class DeliveryStatus {

	public static final String STATUS_DELIVERED = "delivered";
	public static final String STATUS_ERR_BROKEN = "error_broken";
	public static final String STATUS_ERR_MISSING = "error_missing";
	public static final String STATUS_ERR_NO_PICKUP = "error_no_pickup";
	public static final String STATUS_DELIVERED_DESC = "Levererad";
	public static final String STATUS_ERR_BROKEN_DESC = "Fel - trasig";
	public static final String STATUS_ERR_MISSING_DESC = "Fel - saknas";
	public static final String STATUS_ERR_NO_PICKUP_DESC = "Fel - bomkörning";
	
	private String status;
	private String description;
	
	public DeliveryStatus(String status, String description) {
		super();
		this.status = status;
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static List<DeliveryStatus> getFullList() {
		List<DeliveryStatus> list = new ArrayList<>();
		list.add(new DeliveryStatus(STATUS_DELIVERED, STATUS_DELIVERED_DESC));
		list.add(new DeliveryStatus(STATUS_ERR_BROKEN, STATUS_ERR_BROKEN_DESC));
		list.add(new DeliveryStatus(STATUS_ERR_MISSING, STATUS_ERR_MISSING_DESC));
		list.add(new DeliveryStatus(STATUS_ERR_NO_PICKUP, STATUS_ERR_NO_PICKUP_DESC));
		return list;
	}

	public static List<DeliveryStatus> getDefaultList() {
		List<DeliveryStatus> list = new ArrayList<>();
		list.add(new DeliveryStatus(STATUS_DELIVERED, STATUS_DELIVERED_DESC));
		return list;
	}

	
}
