package se.lanteam;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class MailTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			System.out.println("New mail to send");
			Properties props = System.getProperties();
			props.put("mail.smtp.host","ltmail02.lanteam.local");
			props.put("mail.smtp.auth","true");
			props.put("mail.smtp.port", "25");
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("lim@lanteam.se", "LanTeam"));
			msg.setReplyTo(InternetAddress.parse("bjorn.tornqvist@intraservice.goteborg.se", false));
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("tornqvistb@gmail.com", false));
			msg.setSubject("Ett ämne");
			msg.setText("Här kommer ett testmail");
			msg.setHeader("LIM - LanTeam", "E-post från LIM");
			msg.setSentDate(new Date());
			SMTPTransport t =
			    (SMTPTransport)session.getTransport("smtp");
			t.connect("ltmail02.lanteam.local", "lim", "EBs2jJPIBTHC");
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
