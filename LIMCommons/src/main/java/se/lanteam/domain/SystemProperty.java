package se.lanteam.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SystemProperty {

	private String id;
	private String stringValue;
	private Long numberValue;              
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public Long getNumberValue() {
		return numberValue;
	}
	public void setNumberValue(Long numberValue) {
		this.numberValue = numberValue;
	}
	
}
