package se.lanteam.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

import se.lanteam.constants.LimStringUtil;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.Email;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.repository.AttachmentRepository;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.services.PropertyService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class MailSenderService {

	private static final String GENERAL_EMAIL_ERROR = "Fel vid skickande av mail. ";
	
	private static final Logger LOG = LoggerFactory.getLogger(MailSenderService.class);    
    
    private ErrorRepository errorRepo;
    private EmailRepository emailRepo;
    private PropertyService propService;    
    private AttachmentRepository attachmentRepo;
    
	public void checkMailsToSend()  {
		String mailSmtpHost = propService.getString(PropertyConstants.MAIL_SMTPS_HOST);    
	    String mailUsername = propService.getString(PropertyConstants.MAIL_USERNAME);
	    String mailPassword = propService.getString(PropertyConstants.MAIL_PASSWORD);

		List<Email> emails = emailRepo.findEmailsByStatus(StatusConstants.EMAIL_STATUS_NEW);
		LOG.info("Check for emails to send");
		for (Email email : emails) {
			try {
				LOG.debug("New mail to send");
				Properties props = System.getProperties();
				props.put("mail.smtps.host",mailSmtpHost);
				props.put("mail.smtps.auth","true");
				//props.put("mail.smtps.port", "465"); Exchange
				Session session = Session.getInstance(props, null);
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(mailUsername, "LanTeam"));
				msg.setReplyTo(InternetAddress.parse(email.getSender(), false));
				msg.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email.getReceiver(), false));
				msg.setSubject(email.getSubject());
				msg.setText(email.getContent());
				msg.setHeader("LIM - LanTeam", "E-post från LIM");
				msg.setSentDate(new Date());
				if (email.getAttachmentRef() != null && email.getAttachmentRef() > 0) {
					Attachment attachment = attachmentRepo.findOne(email.getAttachmentRef());
					if (attachment != null) {
						BodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setText(email.getContent());
						Multipart multipart = new MimeMultipart();
						multipart.addBodyPart(messageBodyPart);
						messageBodyPart = new MimeBodyPart();
						String contentType = LimStringUtil.NVL(attachment.getContentType(),"image/jpeg");
				        DataSource source = new ByteArrayDataSource(attachment.getFileContent(), contentType);
				        messageBodyPart.setDataHandler(new DataHandler(source));
				        messageBodyPart.setFileName(attachment.getFileName());
				        multipart.addBodyPart(messageBodyPart);
				        msg.setContent(multipart);
					}
				}
				SMTPTransport t =
				    (SMTPTransport)session.getTransport("smtps");
				t.connect(mailSmtpHost, mailUsername, mailPassword);
				t.sendMessage(msg, msg.getAllRecipients());
				LOG.debug("Response: " + t.getLastServerResponse());
				t.close();
			} catch (AddressException e) {
				saveError(GENERAL_EMAIL_ERROR + "AddressException");
			} catch (NoSuchProviderException e) {
				saveError(GENERAL_EMAIL_ERROR + "NoSuchProviderException");
			} catch (SendFailedException e) {
				saveError(GENERAL_EMAIL_ERROR + "SendFailedException");
				e.printStackTrace();
			} catch (MessagingException e) {
				saveError(GENERAL_EMAIL_ERROR + "MessagingException");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				saveError(GENERAL_EMAIL_ERROR + "UnsupportedEncodingException");
			}
			email.setStatus(StatusConstants.EMAIL_STATUS_SENT);
			emailRepo.save(email);
		}
	}    
	
	
	
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}
	
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
	@Autowired
	public void setEmailRepo(EmailRepository emailRepo) {
		this.emailRepo = emailRepo;
	}
	@Autowired
	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}
	@Autowired
	public void setAttachmentRepo(AttachmentRepository attachmentRepo) {
		this.attachmentRepo = attachmentRepo;
	}
}
