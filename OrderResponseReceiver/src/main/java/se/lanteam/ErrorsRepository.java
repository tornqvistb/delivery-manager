package se.lanteam;

import javax.xml.bind.JAXBElement;

import org.springframework.stereotype.Component;

import se.lanteam.ws.gbgintraservice.ObjectFactory;

@Component
public class ErrorsRepository {

	public JAXBElement<String> storeErrorReport() {
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<String> result = factory.createErrorResponseResponseErrorResponseResult("OK");		
		return result;
	}
	
}
