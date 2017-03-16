package se.lanteam.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomField {

	private Long identification;
	private String label;
	private Date creationDate;

	public CustomField() {
		super();
		creationDate = new Date();
	}
	
	@Id
	public Long getIdentification() {
		return identification;
	}
	public void setIdentification(Long id) {
		this.identification = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
