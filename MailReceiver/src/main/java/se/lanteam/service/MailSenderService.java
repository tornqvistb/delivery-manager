package se.lanteam.service;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Email;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.ErrorRepository;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class MailSenderService {

	private static final String GENERAL_EMAIL_ERROR = "Fel vid skickande av mail. ";
	
	private static final Logger LOG = LoggerFactory.getLogger(MailSenderService.class);
    @Value("${mail.host}") 
    private String mailHost;    
    @Value("${mail.username}")
    private String mailUsername;
    @Value("${mail.password}")
    private String mailPassword;
    @Value("${mail.smtps.host}")
    private String smtpHost;
    
    
    private ErrorRepository errorRepo;
    private EmailRepository emailRepo;
        
	public void checkMailsToSend() {
		List<Email> emails = emailRepo.findEmailsByStatus(StatusConstants.EMAIL_STATUS_NEW);
		LOG.info("Check for emails to send");
		for (Email email : emails) {
			try {
				LOG.info("New mail to send");
				Properties props = System.getProperties();
				props.put("mail.smtps.host",smtpHost);
				props.put("mail.smtps.auth","true");
				Session session = Session.getInstance(props, null);
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(email.getSender()));;
				msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email.getReceiver(), false));
				msg.setSubject(email.getSubject());
				msg.setText(email.getContent());
				msg.setHeader("LIM - LanTeam", "E-post från LIM");
				msg.setSentDate(new Date());
				SMTPTransport t =
				    (SMTPTransport)session.getTransport("smtps");
				t.connect("smtp.gmail.com", mailUsername, mailPassword);
				t.sendMessage(msg, msg.getAllRecipients());
				System.out.println("Response: " + t.getLastServerResponse());
				t.close();
			} catch (AddressException e) {
				saveError(GENERAL_EMAIL_ERROR + "AddressException");
			} catch (NoSuchProviderException e) {
				saveError(GENERAL_EMAIL_ERROR + "NoSuchProviderException");
			} catch (SendFailedException e) {
				saveError(GENERAL_EMAIL_ERROR + "SendFailedException");
			} catch (MessagingException e) {
				saveError(GENERAL_EMAIL_ERROR + "MessagingException");
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
}
