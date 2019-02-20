package se.lanteam.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import se.goteborg.generated.ws.client.servicenow.levstatus.GBCA002AExtLeveransStatus;
import se.goteborg.generated.ws.client.servicenow.levstatus.GBCA002AExtLeveransStatus.Status;
import se.goteborg.generated.ws.client.servicenow.levstatus.Header;
import se.goteborg.generated.ws.client.servicenow.levstatus.Interface1;
import se.goteborg.generated.ws.client.servicenow.levstatus.LanTeam02LeveransStatusSOAP;
import se.goteborg.generated.ws.client.servicenow.levstatus.ObjectFactory;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderLine;

public class LevStatusServiceNowClient {
	
	public static final String WS_RETURN_CODE_OK = "0";
		
	protected se.lanteam.ws.Header sendDeliveryStatus(OrderComment orderComment, WSConfig config) throws MalformedURLException {
		ObjectFactory factory = new ObjectFactory();
		GBCA002AExtLeveransStatus deliveryStatus = factory.createGBCA002AExtLeveransStatus();
		deliveryStatus.setArendeId(orderComment.getOrderHeader().getCustomerOrderNumber());
		Status statusRow = factory.createGBCA002AExtLeveransStatusStatus();
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
		
		URL url = new URL(config.getEndPoint());
		
		QName qName = new QName("http://schemas.nevatech.com/services/", "LanTeam02_LeveransStatus_SOAP");
		LanTeam02LeveransStatusSOAP service = new LanTeam02LeveransStatusSOAP(url, qName);
		Interface1 port = service.getLanTeam02LevStatus();
		BindingProvider prov = (BindingProvider)port;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, config.getUserName());
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, config.getPassword());
		
		return toCommonHeader(port.lanTeam02LeveransStatus(deliveryStatus));
	}
	private se.lanteam.ws.Header toCommonHeader(Header header){
		return new se.lanteam.ws.Header(header.getKod(), header.getText());
	}
}
