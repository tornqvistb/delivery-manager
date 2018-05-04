package se.lanteam.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import se.lanteam.constants.StatusConstants;

@Entity
public class Email {

	private Long id;
	private String sender;
	private String receiver;
	private String subject;
	private String content;
	private String status;
	private Date creationDate;
	private Long attachmentRef;
	private String replyTo;
	
	public Email() {
		super();
		creationDate = new Date();
		status = StatusConstants.EMAIL_STATUS_NEW;
		content = "-";
	}
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Long getAttachmentRef() {
		return attachmentRef;
	}
	public void setAttachmentRef(Long attachmentRef) {
		this.attachmentRef = attachmentRef;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	
}
