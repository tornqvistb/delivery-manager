package se.lanteam;

import java.io.File;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

public class MailReceiveTesterOutlookPOP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String mailHost = "outlook.office365.com";    
		String mailUsername = "limtest@visolit.se";
		//String mailUsername = "visolit\\limtest";
		String mailPassword = "KallePelle123";
	    String saveDirectory = "C:/Projekt/lanteam/filedirs/images";
		try {
			System.out.println("Going to check mails");

			final String INBOX_FOLDER = "INBOX";
	        final String PROCESSED_FOLDER = "LIMProcessed";
	        
	        Properties props = getPOPProperties();
	        
			Session emailSession = Session.getInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(mailUsername, mailPassword);
			    }
			});

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect();
			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("Got " + messages.length + " mails!");
			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				String contentType = message.getContentType();
				if (contentType.contains("multipart")) {
					System.out.println("Got mail: " + message.getSubject());					
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            part.saveFile(saveDirectory + File.separator + fileName);
                            /*
                            byte[] array = Files.readAllBytes(new File(saveDirectory + File.separator + fileName).toPath());
                            storeAttachmentOnOrder(array, message.getSubject(), fileName, message);
                            */
                        }
                    }
                } else {
                	System.out.println("No attachment in mail: " + message.getSubject());
                }
			}
			// close the store and folder objects
			emailFolder.close(true);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Properties getPOPProperties() {
		Properties props = new Properties();
		props.put("mail.host", "outlook.office365.com");
		props.put("mail.store.protocol", "pop3s");
	    props.put("mail.pop3s.auth", "true");
	    props.put("mail.pop3s.port", "995");
		return props;
	}

	
	private static Properties getIMAPProperties() {
		Properties props = new Properties();
		props.put("mail.imaps.host", "outlook.office365.com");
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		props.setProperty("mail.imaps.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.imaps.socketFactory.fallback", "false");
		props.setProperty("mail.imaps.port", "993");
		props.setProperty("mail.imaps.socketFactory.port", "993");
		props.setProperty("mail.imaps.auth.plain.disable", "true");
		props.setProperty("mail.imaps.auth.gssapi.disable", "true");
		props.setProperty("mail.imaps.auth.ntlm.disable", "true");
		props.setProperty("mail.imaps.ssl.enable", "true");
		props.setProperty("mail.imaps.ssl.trust", "*");
		return props;
	}
	
}
