package se.lanteam.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ErrorRecord {
	private Long id;
	private Date creationDate;
	private String message;
	private Boolean archived = false;
	
	public ErrorRecord() {
		super();
	}
	public ErrorRecord(String message) {
		super();
		this.creationDate = new Date();
		this.message = message;
	}
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getArchived() {
		return archived;
	}
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}
	@Transient
	public String getCreationDateDisplay() {		
		String result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(creationDate);
		return result;		
	}
	@Transient
	public String getArchivedDisplay() {
		String result = "Ny";
		if (archived) {
			result = "Arkiverad";
		}
		return result;		
	}
	
}
