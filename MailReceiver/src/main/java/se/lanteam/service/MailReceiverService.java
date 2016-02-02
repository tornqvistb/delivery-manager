package se.lanteam.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.lanteam.domain.Attachment;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;

/**
 * Created by david on 2015-01-20.
 */
@Service
public class MailReceiverService {

	private static final String GENERAL_FILE_ERROR = "Fel vid läsning av mail. ";
	private static final String ERROR_INVALID_SUBJECT = GENERAL_FILE_ERROR + "Ämnet i mailet matchar inte någon befintlig order.";
	
	private static final Logger LOG = LoggerFactory.getLogger(MailReceiverService.class);
    @Value("${mail.host}") 
    private String mailHost;    
    @Value("${mail.username}")
    private String mailUsername;
    @Value("${mail.password}")
    private String mailPassword;
    
    private OrderRepository orderRepo;
    private ErrorRepository errorRepo;
    
    public void hello() {
        LOG.info("Hello World!");
        checkMails();
    }
    
	private void checkMails() {
		try {

			// create properties field
			Properties properties = new Properties();

			properties.put("mail.pop3.host", mailHost);
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			//Session emailSession = Session.getDefaultInstance(properties);
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
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
				String contentType = message.getContentType();
				String attachFiles = "";
				String messageContent = "";
				String saveDirectory = "C:/Projekt/lanteam/filedirs/images";
				if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile(saveDirectory + File.separator + fileName);
                            byte[] array = Files.readAllBytes(new File(saveDirectory + File.separator + fileName).toPath());
                            storeAttachmentOnOrder(array, message.getSubject(), fileName);
                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }
 
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
 
			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
	
	private void storeAttachmentOnOrder(byte[] fileContent, String orderNumber, String fileName) throws IOException, MessagingException {
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
		if (orders != null && orders.size() > 0) {
			OrderHeader order = orders.get(0);
			if (order.getEditable()) {
				Attachment attachment = new Attachment();
				attachment.setOrderHeader(order);
				attachment.setFileName(fileName);
				//InputStream initialStream = fileContent.getInputStream();				
				//attachment.setFileContent(new byte[initialStream.available()]);
				attachment.setFileContent(fileContent);
				attachment.setFileSize(Long.valueOf(fileContent.length));
				//attachment.setFileSize(Long.valueOf(initialStream.toString().length()));
				order.setAttachment(attachment);
				orderRepo.save(order);
			} else {
				// Error message
			}
		} else {
			// Error message
		}
	}
	
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
}
