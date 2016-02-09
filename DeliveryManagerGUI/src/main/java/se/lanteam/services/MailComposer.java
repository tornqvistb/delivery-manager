package se.lanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.domain.Email;
import se.lanteam.domain.Equipment;
import se.lanteam.model.CorrectionMailInfo;
import se.lanteam.repository.EmailRepository;

@Service
public class MailComposer {

	private EmailRepository emailRepo;
		
	public void createMail(CorrectionMailInfo mailInfo) {
		if (mailInfo.getModifiedEquipment().size() > 0) {
			Email email = new Email();
			StringBuffer sb = new StringBuffer();
			sb.append("Hej!\n\n");
			sb.append("Följande beställning har korrigerats hos leverantör:\n");
			sb.append("Order: " + mailInfo.getOrderHeader().getCustomerSalesOrder() + "\n\n");
			sb.append("Justerad utrustning:\n\n");
			for (Equipment equip : mailInfo.getModifiedEquipment()) {
				sb.append("- Före detta serienummer/söld-id: " + equip.getPreviousSerialNo() + " / " + equip.getPreviousStealingTag());
				sb.append("- Nytt serienummer/söld-id: " + equip.getSerialNo() + " / " + equip.getStealingTag() + "\n");
			}
			sb.append("\n" + "Med vänlig hälsning LanTeam");
			email.setContent(sb.toString());
			email.setSender("lim.lanteam@gmail.com");
			email.setReceiver("tornqvistb@gmail.com");
			emailRepo.save(email);
		}
	}
	
	@Autowired
	public void setEmailRepo(EmailRepository emailRepo) {
		this.emailRepo = emailRepo;
	}
	
}
