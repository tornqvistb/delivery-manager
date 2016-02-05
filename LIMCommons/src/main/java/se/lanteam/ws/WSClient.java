package se.lanteam.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import gbca002a_ext_leveransstatus.gbca002a.externalartefacts.gbca.esb.staden.GBCA002AExtLeveransStatus;
import gbca002a_ext_leveransstatus.gbca002a.externalartefacts.gbca.esb.staden.GBCA002AExtLeveransStatus.Status;
import gbca003a_ext_leveransavisering.esb.staden.GBCA003AExtLeveransAvisering;
import gbca003a_ext_leveransavisering.esb.staden.ObjectFactory;
import gbca003a_leveransavisering.esb.staden.GBCA003ALeveransAvisering;
import gbca003a_leveransavisering.esb.staden.GBCA003ALeveransAvisering_Service;
import header.common.esb.staden._1.Header;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;

public class WSClient {
	
	public static final String WS_RETURN_CODE_OK = "0";
	
	public Header sendOrderDelivery(OrderHeader orderHeader, WSConfig config) throws MalformedURLException {
		ObjectFactory factory = new ObjectFactory();
		header.common.esb.staden._1.ObjectFactory headerfactory = new header.common.esb.staden._1.ObjectFactory();
		Header header = headerfactory.createHeader();
		GBCA003AExtLeveransAvisering delivery = factory.createGBCA003AExtLeveransAvisering();
		GBCA003AExtLeveransAvisering.Body body = factory.createGBCA003AExtLeveransAviseringBody();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering avisering = factory.createGBCA003AExtLeveransAviseringBodyLeveransAvisering();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Head head = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringHead();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Head.Address address = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringHeadAddress();
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Line line;
		GBCA003AExtLeveransAvisering.Body.LeveransAvisering.Line.Info info;
		// header
		header.setKod("1"); // Sätt mer header värden
		delivery.setHeader(header);
		head.setOrderDate(orderHeader.getOrderDateAsString());
		head.setOrderNumber(orderHeader.getOrderNumber());
		head.setYourReference("");
		head.setYourPurchaseOrder(orderHeader.getCustomerOrderNumber());
		head.setYourSalesOrder(orderHeader.getCustomerSalesOrder());
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
			line = factory.createGBCA003AExtLeveransAviseringBodyLeveransAviseringLine();
			line.setLineId(String.valueOf(orderLine.getRowNumber()));
			line.setLineStatus("");
			line.setYourOrderLine(String.valueOf(orderLine.getRowNumber()));
			line.setItemId(orderLine.getArticleNumber());
			line.setQty(String.valueOf(orderLine.getTotal()));
			line.setPrice("");
			line.setTotal("");
			line.setCurrency("");
			line.setRequestedDelDate(""); // Värde här? finns från Atea
			line.setEstimatedDelDate(""); // Värde här? finns från Atea
			line.setDescription("");
			line.setSpecification("");
			line.setDescription2("");
			line.setSpecification2("");
			line.setActualDelDate(""); // Värde här? finns från Atea
			line.setOrderLineComment("");			
			
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
		
		//URL url = new URL("http://esbat.goteborg.se/Wsdl/GBCA003A_LeveransAvisering_https_.wsdl");
		URL url = new URL(config.getEndPoint());
		
		QName qName = new QName("http://lanteam.se/", "GBCA003A_LeveransAvisering");
		GBCA003ALeveransAvisering_Service service = new GBCA003ALeveransAvisering_Service(url, qName);
		GBCA003ALeveransAvisering port = service.getBasicHttpBindingGBCA003ALeveransAvisering();
		BindingProvider prov = (BindingProvider)port;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, config.getUserName());
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, config.getPassword());

		return port.gbca003ALeveransAvisering(delivery);

	}
	
	public Header sendDeliveryStatus(OrderComment orderComment, WSConfig config) throws MalformedURLException {
		header.common.esb.staden._1.ObjectFactory headerfactory = new header.common.esb.staden._1.ObjectFactory();
		Header header = headerfactory.createHeader();
		gbca002a_ext_leveransstatus.gbca002a.externalartefacts.gbca.esb.staden.ObjectFactory objectFactory = new gbca002a_ext_leveransstatus.gbca002a.externalartefacts.gbca.esb.staden.ObjectFactory();
		GBCA002AExtLeveransStatus deliveryStatus = objectFactory.createGBCA002AExtLeveransStatus();
		deliveryStatus.setArendeId(orderComment.getOrderHeader().getCustomerOrderNumber());
		Status statusRow = objectFactory.createGBCA002AExtLeveransStatusStatus();
		statusRow.setComment(orderComment.getMessage());
		statusRow.setDeliveryDate(""); // Skall vi sätta värde här? Inmatas via GUI?
		if (orderComment.relatesToOrderLine()) {
			statusRow.setLineId(orderComment.getOrderLine());
			for (OrderLine line : orderComment.getOrderHeader().getOrderLines()) {
				if (statusRow.getLineId().equals(String.valueOf(line.getRowNumber()))) {
					statusRow.setItemId(line.getArticleNumber());					
					statusRow.setItemDescription(line.getArticleDescription());					
				}
			}
		} else {
			statusRow.setLineId("");
			statusRow.setItemId("");
			statusRow.setItemDescription("");
		}
		deliveryStatus.getStatus().add(statusRow);
		
		//URL url = new URL("http://esbat.goteborg.se/Wsdl/GBCA002A_LeveransStatus_https_.wsdl");
		URL url = new URL(config.getEndPoint());
		
		QName qName = new QName("http://lanteam.se/", "GBCA002A_LeveransStatus");
		GBCA003ALeveransAvisering_Service service = new GBCA003ALeveransAvisering_Service(url, qName);
		GBCA003ALeveransAvisering port = service.getBasicHttpBindingGBCA003ALeveransAvisering();
		BindingProvider prov = (BindingProvider)port;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, config.getUserName());
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, config.getPassword());
		
		return header;
	}
	
}
