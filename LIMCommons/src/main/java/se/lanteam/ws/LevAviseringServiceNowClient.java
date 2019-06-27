package se.lanteam.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.codehaus.groovy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.StringUtils;
import se.goteborg.generated.ws.client.servicenow.levavisering.GBCA003AExtLeveransAvisering;
import se.goteborg.generated.ws.client.servicenow.levavisering.Header;
import se.goteborg.generated.ws.client.servicenow.levavisering.IVirtualInterface;
import se.goteborg.generated.ws.client.servicenow.levavisering.ObjectFactory;
import se.goteborg.generated.ws.client.servicenow.levavisering.LanTeam03LeveransAviseringSOAP;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;

public class LevAviseringServiceNowClient {
	
	public static final String WS_RETURN_CODE_OK = "0";
	
	private static final Logger LOG = LoggerFactory.getLogger(LevAviseringServiceNowClient.class);
	
	public se.lanteam.ws.Header sendOrderDelivery(OrderHeader orderHeader, WSConfig config) throws MalformedURLException {
		ObjectFactory factory = new ObjectFactory();
		Header header = factory.createHeader();
		GBCA003AExtLeveransAvisering delivery = factory.createGBCA003AExtLeveransAvisering();
		GBCA003AExtLeveransAvisering.Body body = factory.createGBCA003AExtLeveransAviseringBody();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering avisering = factory.createGBCA003AExtLeveransAviseringBodyLeveransAvisering();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Head head = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringHead();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Head.Address address = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringHeadAddress();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Line line;
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Line.Info info;
		// header
		header.setKod("1"); // Sätt mer header värden
		header.setPartnerIn("LanTeam");
		delivery.setHeader(header);
		head.setOrderDate(orderHeader.getOrderDateAsString());
		head.setOrderNumber(orderHeader.getOrderNumber());
		head.setYourReference("");
		// Sales Order och Order Number kastas om här. Beror på att de kommer så från Visma.
		head.setYourPurchaseOrder(orderHeader.getCustomerSalesOrder());
		head.setYourSalesOrder(orderHeader.getCustomerOrderNumber());
		head.setOurReference(orderHeader.getOrderNumber());
		head.setTermsPay("");
		head.setTermsDel("");
		head.setDeliverymethod("");
		head.setGoodsMark(orderHeader.getContact1Name());
		head.setHeadStatus("");		
		//address
		address.setName(orderHeader.getCustomerName());
		address.setAddress(orderHeader.getDeliveryPostalAddress1());
		address.setStreet(orderHeader.getPostalAddress2());
		address.setZipCode(orderHeader.getPostalCode());
		address.setCity(orderHeader.getCity());
		address.setCountryCode("SE");
		address.setPhone(orderHeader.getContact1Phone());
		address.setFax("");
		head.getAddress().add(address);
		// avisering
		avisering.setHead(head);		
		avisering.setPartnerId("");
		avisering.setTransactionId("");
		// orderlines
		for (OrderLine orderLine : orderHeader.getOrderLines()) {
			if (orderLine.getCustomerRowNumber() != null && orderLine.getCustomerRowNumber() > 0) {
				line = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLine();
				line.setLineId(String.valueOf(orderLine.getRowNumber()));
				line.setLineStatus("");
				line.setYourOrderLine(String.valueOf(orderLine.getRowNumber()));
				line.setItemId(orderLine.getArticleNumber());
				line.setQty(String.valueOf(orderLine.getTotal()));
				line.setPrice("");
				line.setTotal("");
				line.setCurrency("");
				line.setRequestedDelDate(""); // Värde här? finns från Atea - skicka tomt
				line.setEstimatedDelDate(""); // Värde här? finns från Atea - skicka tomt
				line.setDescription("");
				line.setSpecification("");
				line.setDescription2("");
				line.setSpecification2("");
				line.setActualDelDate(""); // Värde här? finns från Atea - Datumet när detta skickas
				String olc = orderLine.getLeasingNumber();
				if (orderLine.getRequestItemNumber() != null && orderLine.getRequestItemNumber().length() > 0) {
					olc = olc + "/" + orderLine.getRequestItemNumber();
				}
				line.setOrderLineComment(olc); // SN 2.1			
				
				for (Equipment equipment : orderLine.getEquipments()) {
					info = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLineInfo();
					info.setInvNo(equipment.getStealingTag());
					info.setSerialNo(equipment.getSerialNo());
					line.getInfo().add(info);
				}
				avisering.getLine().add(line);
			}
		}
		LOG.debug("Avisering to SN: " + avisering.toString());
		body.setLeveransAvisering(avisering);
		delivery.setBody(body);
		
		URL url = new URL(config.getEndPoint());
		
		QName qName = new QName("http://schemas.nevatech.com/services/", "LanTeam03_LeveransAvisering_SOAP");
		LanTeam03LeveransAviseringSOAP service = new LanTeam03LeveransAviseringSOAP(url, qName);
		IVirtualInterface port = service.getLanTeam03LevAvisering();
		BindingProvider prov = (BindingProvider)port;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, config.getUserName());
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, config.getPassword());

		return toCommonHeader(port.lanTeam03LeveransAvisering(delivery));

	}
	private se.lanteam.ws.Header toCommonHeader(Header header){
		return new se.lanteam.ws.Header(header.getKod(), header.getText());
	}		
}
