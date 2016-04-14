package se.lanteam.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.Email;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PropertyService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class MailReceiverService {

	private static final String GENERAL_FILE_ERROR = "Fel vid läsning av mail. ";
	private static final String ERROR_INVALID_SUBJECT = GENERAL_FILE_ERROR + "Ämnet i mailet matchar inte någon befintlig order. Ämne: ";
	private static final String ERROR_INVALID_ORDER_STATUS = GENERAL_FILE_ERROR + "Filen kunde inte sparas på ordern, då den inte är redigerbar. Order, Filnamn: ";
	private static final String THANKS_MAIL_REPLY = "Tack för ditt mail. Bilden %s är nu bifogad till order %s.";
	private static final String MAIL_SUBJECT = "Bild mottagen";
	
	private static final Logger LOG = LoggerFactory.getLogger(MailReceiverService.class);
    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    private PropertyService propService;
    private EmailRepository emailRepo;
        
	public void checkMails() {		
		String mailHost = propService.getString(PropertyConstants.MAIL_HOST);    
	    String mailUsername = propService.getString(PropertyConstants.MAIL_USERNAME);
	    String mailPassword = propService.getString(PropertyConstants.MAIL_PASSWORD);
	    String saveDirectory = propService.getString(PropertyConstants.FILE_IMAGE_FOLDER);
		try {
	        LOG.info("Going to check mails");
			// create properties field
			Properties properties = new Properties();

			properties.put("mail.pop3.host", mailHost);
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(mailUsername, mailPassword);
			    }
			});

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect(mailHost, mailUsername, mailPassword);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);			

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			LOG.info("Got " + messages.length + " mails!");
			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				String contentType = message.getContentType();
				LOG.info("Contenttype: " + contentType);
				if (contentType.contains("multipart")) {
					LOG.info("Got mail: " + message.getSubject());					
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            part.saveFile(saveDirectory + File.separator + fileName);
                            byte[] array = Files.readAllBytes(new File(saveDirectory + File.separator + fileName).toPath());
                            storeAttachmentOnOrder(array, message.getSubject(), fileName, message);
                        }
                    }
                } else {
                	LOG.info("No attachment in mail: " + message.getSubject());
                }
			}
			// close the store and folder objects
			emailFolder.close(true);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
	
	private void storeAttachmentOnOrder(byte[] fileContent, String orderNumber, String fileName, Message message) throws IOException, MessagingException {
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
		String resultText = "";
		if (orders != null && orders.size() > 0) {
			OrderHeader order = orders.get(0);
			if (order.getEditable()) {
				Attachment attachment = new Attachment();
				attachment.setOrderHeader(order);
				attachment.setFileName(fileName);
				attachment.setFileContent(fileContent);
				attachment.setFileSize(Long.valueOf(fileContent.length));
				order.setAttachment(attachment);
				order.setOrderStatusByProgress();
				orderRepo.save(order);
				resultText = String.format(THANKS_MAIL_REPLY, fileName, orderNumber);
			} else {
				resultText = ERROR_INVALID_ORDER_STATUS + orderNumber + ", " + fileName;
				saveError(resultText);
			}
		} else {
			resultText = ERROR_INVALID_SUBJECT + orderNumber + ", filnamn: " + fileName;
			saveError(resultText);
		}
		createMail(resultText, message);
		
	}
	
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}
	private void createMail(String content, Message message) throws MessagingException {
		Email email = new Email();
		email.setContent(content);
		email.setSubject(MAIL_SUBJECT);
		Address[] receivers = message.getAllRecipients();
		Address[] senders = message.getFrom();
		if (receivers != null && receivers.length > 0 && senders != null && senders.length > 0) {
			email.setSender(receivers[0].toString());
			email.setReceiver(senders[0].toString());
			emailRepo.save(email);			
		}
	}
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
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
}
