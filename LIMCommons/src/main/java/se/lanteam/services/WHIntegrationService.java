package se.lanteam.services;

import static se.lanteam.constants.PropertyConstants.FILE_OUTGOING_COPY_FOLDER;
import static se.lanteam.constants.PropertyConstants.FILE_OUTGOING_WH_FOLDER;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import se.lanteam.domain.ErrorRecord;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.ErrorRepository;

@Service
public class WHIntegrationService {
	private static final String GENERAL_ERROR = "Fel vid överföring av orderleverans till kund. ";
	private static final String FILE_EXPORT_ERROR = GENERAL_ERROR + "Ett fel uppstod när fil till Lexit skulle lagras på disk, order: ";
	private static final String FILE_ENDING = ".txt";

	@Autowired
	private ErrorRepository errorRepo;
	@Autowired
	private PropertyService propService;
	
	public void createFileToWarehouseSystem(OrderHeader order) {
		String fileTransmitFolder = propService.getString(FILE_OUTGOING_WH_FOLDER);
		String fileTransmitCopyFolder = propService.getString(FILE_OUTGOING_COPY_FOLDER);
		
		String fileData = order.getOrderNumber();
		
		try {
			Path path = Paths.get(fileTransmitFolder + "/" + order.getOrderNumber() + FILE_ENDING);
			Files.deleteIfExists(path);
			FileWriter writer = new FileWriter(fileTransmitFolder + "/" + order.getOrderNumber() + FILE_ENDING);
			writer.write(fileData);
			writer.close();
			// Save copy
			if (!StringUtils.isEmpty(fileTransmitCopyFolder)) {
				Path pathCopy = Paths.get(fileTransmitCopyFolder + "/" + order.getOrderNumber() + FILE_ENDING);
				Files.deleteIfExists(pathCopy);
				FileWriter writerCopy = new FileWriter(fileTransmitCopyFolder + "/" + order.getOrderNumber() + FILE_ENDING);
				writerCopy.write(fileData);
				writerCopy.close();
			}

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
