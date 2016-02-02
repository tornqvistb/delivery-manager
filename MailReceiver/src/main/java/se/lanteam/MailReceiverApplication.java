package se.lanteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SchedulerConfig.class})
public class MailReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailReceiverApplication.class, args);
	}
}
