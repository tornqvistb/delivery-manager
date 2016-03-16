package se.lanteam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import se.lanteam.service.MailSenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MailReceiverApplication.class)
@WebAppConfiguration
public class MailReceiverApplicationTests {

	@Autowired
    private MailSenderService senderService;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testSenderService() {
		
		senderService.checkMailsToSend();
	}

}
