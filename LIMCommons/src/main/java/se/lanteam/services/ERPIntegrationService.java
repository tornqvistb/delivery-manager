package se.lanteam.services;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.visma.Order;
import se.lanteam.visma.Orderrad;

@Service
public class ERPIntegrationService {
	private static final String GENERAL_ERROR = "Fel vid överföring av orderleverans till kund. ";
	private static final String FILE_EXPORT_ERROR = GENERAL_ERROR + "Ett fel uppstod när fil till Visma skulle lagras på disk, order: ";

	@Autowired
	private ErrorRepository errorRepo;
	@Autowired
	private PropertyService propService;
	
	public void createFileToBusinessSystem(OrderHeader order) {
		String fileTransmitFolder = propService.getString(PropertyConstants.FILE_OUTGOING_FOLDER);
		Order vismaOrder = new Order();
		vismaOrder.setOrdernummer(Integer.parseInt(order.getOrderNumber()));
		vismaOrder.setLeveransflagga(getLeveransflaggaForOrder(order));
		List<Orderrad> vismaRows = new ArrayList<Orderrad>();
		for (OrderLine line : order.getOrderLines()) {
			Orderrad vismaRow = new Orderrad();
			vismaRow.setArtikelnummer(line.getArticleNumber());
			vismaRow.setOrderrad(line.getRowNumber());
			if (line.getCustomerRowNumber() != null) {
				vismaRow.setKundradnummer(line.getCustomerRowNumber());	
			} else {
				vismaRow.setKundradnummer(0);
			}
			List<String> vismaSnr = new ArrayList<String>();
			for (Equipment equipment : line.getEquipments()) {
				vismaSnr.add(equipment.getSerialNo());        				
			}
			vismaRow.setSerienummer(vismaSnr);
			vismaRow.setLeasingnummer(line.getLeasingNumber());
			vismaRows.add(vismaRow);
		}
		vismaOrder.setOrderrader(vismaRows);
		Gson gson = new Gson();
		String json = gson.toJson(vismaOrder);
		try {
			Path path = Paths.get(fileTransmitFolder + "/" + order.getOrderNumber() + ".json");
			Files.deleteIfExists(path);
			FileWriter writer = new FileWriter(fileTransmitFolder + "/" + order.getOrderNumber() + ".json");
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			saveError(FILE_EXPORT_ERROR + order.getOrderNumber());
		}		
	}
	
	private Integer getLeveransflaggaForOrder(OrderHeader order) {
		Integer result = 0;
		if (order.getCustomerGroup().getDeliveryFlagToERP()) {
			result = 1;
		}
		return result;
	}
	private void saveError(String errorText) {
		errorRepo.save(new ErrorRecord(errorText));
	}

}
