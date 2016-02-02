package se.lanteam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import se.lanteam.ws.gbgintraservice.ErrorResponse;
import se.lanteam.ws.gbgintraservice.ErrorResponseResponse;

@Endpoint
public class ErrorsEndpoint {
	//private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
	private static final String NAMESPACE_URI = "http://lanteam.se/ws/GbgIntraservice";
	private ErrorsRepository errorsRepository;

	@Autowired
	public ErrorsEndpoint(ErrorsRepository errorsRepository) {
		this.errorsRepository = errorsRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ErrorResponse")
	@ResponsePayload
	public ErrorResponseResponse getResponse(@RequestPayload ErrorResponse request) {
		ErrorResponseResponse response = new ErrorResponseResponse();
		response.setErrorResponseResult(errorsRepository.storeErrorReport());

		return response;
	}
}
