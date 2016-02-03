
package se.lanteam.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class OrderDeliveryConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPaths("se.intraservice.orderdelivery");
		return marshaller;
	}

	@Bean
	public OrderDeliveryClient wsClient(Jaxb2Marshaller marshaller) {
		OrderDeliveryClient client = new OrderDeliveryClient();
		client.setDefaultUri("https://esbat.goteborg.se/GBCA003A_LeveransAvisering");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
