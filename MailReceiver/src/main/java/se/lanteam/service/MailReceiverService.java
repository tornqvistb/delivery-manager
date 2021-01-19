package se.lanteam.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.mail.Address;
import javax.mail.Flags;
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
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.Email;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.EmailRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.ERPIntegrationService;
import se.lanteam.services.PropertyService;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
@Transactional
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
    private ERPIntegrationService erpService;

	public void checkMails() {		
		String mailHost = propService.getString(PropertyConstants.MAIL_HOST);    
	    String mailUsername = propService.getString(PropertyConstants.MAIL_USERNAME);
	    String mailPassword = propService.getString(PropertyConstants.MAIL_PASSWORD);
	    String saveDirectory = propService.getString(PropertyConstants.FILE_IMAGE_FOLDER);
		try {
	        LOG.debug("Going to check incoming mails");
			// create properties field
	        Properties props = System.getProperties();
	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	        final String INBOX_FOLDER = "INBOX";
	        final String PROCESSED_FOLDER = "LIMProcessed";
	        // Set manual Properties
	        props.setProperty("mail.imaps.socketFactory.class", SSL_FACTORY);
	        props.setProperty("mail.imaps.socketFactory.fallback", "false");
	        props.setProperty("mail.imaps.port", "993");
	        props.setProperty("mail.imaps.socketFactory.port", "993");
	        props.put("mail.imaps.host", mailHost);
	        
	        props.setProperty("mail.imaps.partialfetch", "false");
	        props.setProperty("mail.imaps.fetchsize", "2000000");	        
	        
			Session emailSession = Session.getInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(mailUsername, mailPassword);
			    }
			});

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("imaps");

			store.connect(mailHost, mailUsername, mailPassword);

			// create the folder object and open it
			Folder emailFolder = store.getFolder(INBOX_FOLDER);
			emailFolder.open(Folder.READ_WRITE);
			
			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			
			LOG.debug("Got " + messages.length + " mails!");
			
			// Copy messages to archive folder

			Folder processedFolder = store.getFolder(PROCESSED_FOLDER);
			
			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				if (messageIsValid(message)) {
					LOG.debug("Got mail: " + message.getSubject());					
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {                    	
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        LOG.debug("Disposition: " + part.getDisposition());
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || Part.INLINE.equalsIgnoreCase(part.getDisposition())) {                        	
                            // this part is attachment
                            String fileName = part.getFileName();
                        	if (isImage(fileName)) {
                        		LOG.debug("Contains image");	
	                            String fileNameWithPath = saveDirectory + File.separator + fileName;
                        		LOG.debug("Before save file");	
	                            part.saveFile(fileNameWithPath);
                        		LOG.debug("Before new file");	
	                            File origFile = new File(fileNameWithPath);
                        		LOG.debug("Before compressed file name");	
	                            String compressedFileNameWithPath = fileNameWithPath + ".comp.jpg";
                        		LOG.debug("Before compressFile");	
	                            compressFile(origFile, compressedFileNameWithPath);                            
                        		LOG.debug("Before readBytes");	
	                            byte[] array = Files.readAllBytes(new File(compressedFileNameWithPath).toPath());
                        		LOG.debug("Before storeAttachmentOnOrder");	
	                            storeAttachmentOnOrder(array, message.getSubject(), fileName, message);
                        		LOG.debug("Contains image");	
                        	}
                        }
                    }
                    emailFolder.copyMessages(new Message[]{message}, processedFolder);
                } else {
                	LOG.debug("No attachment in mail: " + message.getSubject());
                }
				message.setFlag(Flags.Flag.DELETED, true);
				
			}
			// close the store and folder objects
			emailFolder.close(true);
			store.close();

		} catch (NoSuchProviderException e) {
			LOG.error("NoSuchProviderException: " + e.getMessage(), e);
		} catch (MessagingException e) {
			LOG.error("MessagingException: " + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage(), e);
		}
	}    
	private boolean messageIsValid(Message message) {
		try {
			if (message != null && message.getFrom() != null && message.getFrom().length > 0) {
				boolean domainOK = false;
				boolean contentOK = false;
				String fromAdress = message.getFrom()[0].toString().toLowerCase();
				LOG.debug("Mail sender: " + fromAdress);
				String[] validDomains = propService.getString(PropertyConstants.VALID_EMAIL_DOMAINS_FOR_REPLY).split(",");
				for (String domain : validDomains) {
					domain = domain.trim().toLowerCase();
					if (fromAdress.contains(domain.trim().toLowerCase())) {
						LOG.debug("From adress: " + fromAdress + " contains domain " + domain);
						domainOK = true;
						break;
					}
				}
				if (domainOK) {
					String contentType = message.getContentType();
					if (contentType.contains("multipart")) {
						contentOK = true;
					}					
				}
				return domainOK && contentOK;
			} else {
				LOG.debug("Sender is empty in mail!");
			}
		} catch (MessagingException e) {			
			LOG.error("MessagingException: " + e.getMessage());
		}
		return false;
	}
	
	private boolean isImage(String fileName) {
		boolean result = false;
		
		if (StringUtils.hasText(fileName)) {
			String fileNameLC = fileName.toLowerCase();
			if (fileNameLC.endsWith("jpg") || fileNameLC.endsWith("png") || fileNameLC.endsWith("gif")) {
				result = true;
			}
		}
		return result;
	}
	
	private void compressFile(File origFile, String fileName) throws IOException {
		File compressedFile = new File(fileName);
		InputStream is = new FileInputStream(origFile);
        OutputStream os = new FileOutputStream(compressedFile);
        float quality = 0.5f;
        // create a BufferedImage as the result of decoding the supplied InputStream
        BufferedImage image = ImageIO.read(is);
        // get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()){
        	os.close();
            throw new IllegalStateException("No writers found");
        }
        ImageWriter writer = (ImageWriter) writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();
        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        // appends a complete image stream containing a single image and
        // associated stream and image metadata and thumbnails to the output
        writer.write(null, new IIOImage(image, null, null), param);
        // close all streams
        is.close();
        os.close();
        ios.close();
        writer.dispose();
	}
	
	private void storeAttachmentOnOrder(byte[] fileContent, String orderNumber, String fileName, Message message) throws IOException, MessagingException {
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
		String resultText = "";
		if (orders != null && orders.size() > 0) {
			OrderHeader order = orderRepo.findOne(orders.get(0).getId());
			if (order.getEditable() || order.getTransferringToCustomer()) {
				Attachment attachment = new Attachment();
				attachment.setOrderHeader(order);
				attachment.setFileName(fileName);
				attachment.setFileContent(fileContent);
				attachment.setFileSize(Long.valueOf(fileContent.length));
				attachment.setContentType("image/jpeg");
				order.setAttachment(attachment);
				boolean transferringToCutomer = order.getTransferringToCustomer();
				order.setOrderStatusByProgress(false);
				orderRepo.save(order);
				resultText = String.format(THANKS_MAIL_REPLY, fileName, orderNumber);
				if (transferringToCutomer) {
					erpService.createFileToBusinessSystem(order);
				}
			} else {
				resultText = ERROR_INVALID_ORDER_STATUS + orderNumber + ", " + fileName;
				saveError(resultText);
			}
		} else {
			resultText = ERROR_INVALID_SUBJECT + orderNumber + ", filnamn: " + fileName;
			saveError(resultText);
		}
		if (messageIsValid(message)) {
			createMail(resultText, message);
		}
		
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
	@Autowired
	public void setERPService(ERPIntegrationService erpService) {
		this.erpService = erpService;
	}
}


