package se.lanteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class DeliveryManagerGuiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryManagerGuiApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	         return application.sources(DeliveryManagerGuiApplication.class);
	}
}
