package se.lanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.Email;
import se.lanteam.domain.Equipment;
import se.lanteam.model.CorrectionMailInfo;
import se.lanteam.repository.EmailRepository;

@Service
public class MailComposer {

	private EmailRepository emailRepo;
	private PropertyService propService;
		
	public void createMail(CorrectionMailInfo mailInfo) {
		if (mailInfo.getModifiedEquipment().size() > 0) {
			Email email = new Email();
			StringBuffer sb = new StringBuffer();
			sb.append("Hej!\n\n");
			sb.append("Följande beställning har korrigerats hos leverantör Visolit:\n");
			sb.append("Order: " + mailInfo.getOrderHeader().getCustomerSalesOrder() + "\n\n");
			sb.append("Lista på justerad utrustning:\n\n");
			for (Equipment equip : mailInfo.getModifiedEquipment()) {
				sb.append("Utrustning:\n");
				sb.append("Före detta serienummer/stöld-id: " + equip.getPreviousSerialNo() + " / " + equip.getPreviousStealingTag() + "\n");
				sb.append("Nytt serienummer/stöld-id: " + equip.getSerialNo() + " / " + equip.getStealingTag() + "\n\n");
			}
			sb.append("Med vänlig hälsning \nVisolit");
			email.setContent(sb.toString());
			email.setSubject("Korrigerat serie- och/eller stöldskyddsnummer i beställning");
			email.setSender(propService.getString(PropertyConstants.ORDER_CORRECTION_MAIL_SENDER));
			email.setReceiver(propService.getString(PropertyConstants.ORDER_CORRECTION_MAIL_RECEIVER));
			emailRepo.save(email);
		}
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
