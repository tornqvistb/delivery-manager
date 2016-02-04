package se.lanteam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import se.lanteam.domain.OrderHeader;
import se.lanteam.wsclient.service.SampleService;


public class OrderDeliveryClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(OrderDeliveryClient.class);
	
	public void sendOrderDelivery(OrderHeader orderHeader) {
		SampleService service;
		/*
		ObjectFactory factory = new ObjectFactory();
		Header header = factory.createHeader();
		GBCA003AExtLeveransAvisering delivery = factory.createGBCA003AExtLeveransAvisering();
		GBCA003AExtLeveransAvisering.Body body = factory.createGBCA003AExtLeveransAviseringBody();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering avisering = factory.createGBCA003AExtLeveransAviseringBodyLeveransAvisering();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Head head = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringHead();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Head.Address address = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringHeadAddress();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Line line = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLine();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Line.Info info = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLineInfo();
		// header
		header.setKod("1"); // Sätt mer header värden
		delivery.setHeader(header);
		
		head.setOrderNumber(orderHeader.getOrderNumber()); // Sätt mer värden på head
		address.setAddress(orderHeader.getDeliveryPostalAddress1());
		address.setStreet(orderHeader.getPostalAddress2()); // Sätt mer värden på address
		head.getAddress().add(address);
		avisering.setHead(head);
		avisering.setPartnerId("??");
		avisering.setTransactionId("??");
		for (OrderLine orderLine : orderHeader.getOrderLines()) {
			line = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLine();
			line.setLineId(String.valueOf(orderLine.getRowNumber()));
			line.setItemId(orderLine.getArticleNumber()); // Sätt mer värden på line
			for (Equipment equipment : orderLine.getEquipments()) {
				info = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLineInfo();
				info.setInvNo(equipment.getStealingTag());
				info.setSerialNo(equipment.getSerialNo());
				line.getInfo().add(info);
			}
			avisering.getLine().add(line);
		}
		body.setLeveransAvisering(avisering);
		delivery.setBody(body);
		getWebServiceTemplate() 
			.marshalSendAndReceive(
				"http://esbat.goteborg.se/Wsdl/GBCA003A_LeveransAvisering_https_",
				delivery,
				new SoapActionCallback("http://esbat.goteborg.se/Wsdl/GBCA003A_LeveransAvisering_https_"));
		*/
		// body
		//body.s
		
		//delivery.s
		// info
		//info.s
		//delivery.setBody(value);
	}
/*
	public GetMortgagePaymentResponse getMortagePaymentByNumber(Integer number) {

		GetMortgagePayment request = new GetMortgagePayment();
		request.setAnnualInsurance(number);
		request.setAnnualTax(number);
		request.setInterest(number);
		request.setLoanAmount(number);
		request.setYears(number);

		log.info("Requesting mortgage for " + number);
		
		GetMortgagePaymentResponse response = (GetMortgagePaymentResponse) getWebServiceTemplate() 
				.marshalSendAndReceive(
						"http://www.webservicex.net/mortgage.asmx",
						request,
						new SoapActionCallback("http://www.webserviceX.NET/GetMortgagePayment"));
		printResponse(response);
		return response;
	}

	public void printResponse(GetMortgagePaymentResponse response) {

		MortgageResults results = response.getGetMortgagePaymentResult();

		if (results != null) {
			log.info("results:");
			log.info(results.getMonthlyInsurance() + " - " + 
					results.getMonthlyPrincipalAndInterest() + " - " + 
					results.getMonthlyTax() + " - " +
					results.getTotalPayment());
		} else {
			log.info("No results received");
		}
	}
*/
}
