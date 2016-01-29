package se.lanteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SchedulerConfig.class})
public class OrderCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderCollectorApplication.class, args);
	}
}
