package se.lanteam.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.DeliveryReportField;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.domain.ReportsConfig;
import se.lanteam.repository.CustomerGroupRepository;

@Service
public class ReportConsolidationService {
	
	private static final String ORDER_CUSTOM_FIELD_PREFIX = "ohCustomField";
	
	private static final String EQUIPMENT_CUSTOM_FIELD_PREFIX = "eqCustomAttribute";

	@Autowired
	private CustomerGroupRepository custGroupRepo;
	
	public void updateAllReportLabels() {
		List<CustomerGroup> allGroups = custGroupRepo.findAll();
		for (CustomerGroup group : allGroups) {
			updateDeliveryReportLabels(group);
		}
	}
	
	public void updateDeliveryReportLabels(CustomerGroup customerGroup) {
		customerGroup = custGroupRepo.findOne(customerGroup.getId());
		ReportsConfig reportsConfig = customerGroup.getReportsConfig();
		for (DeliveryReportField reportField : reportsConfig.getReportFields()) {
			if (reportField.getFieldName().startsWith(ORDER_CUSTOM_FIELD_PREFIX)) {
				int fieldNumber = Integer.parseInt(reportField.getFieldName().substring(ORDER_CUSTOM_FIELD_PREFIX.length()));
				reportField.setLabel(getLabelFromCustomField(customerGroup, fieldNumber, reportField.getLabel()));
			} else if (reportField.getFieldName().startsWith(EQUIPMENT_CUSTOM_FIELD_PREFIX)) {
				int fieldNumber = Integer.parseInt(reportField.getFieldName().substring(EQUIPMENT_CUSTOM_FIELD_PREFIX.length()));
				reportField.setLabel(getEquipmentLabelFromCustomField(customerGroup.getRegistrationConfig(), fieldNumber, reportField.getLabel()));
			}
		}
		custGroupRepo.save(customerGroup);		
	}

	private String getLabelFromCustomField(CustomerGroup customerGroup, int fieldNumber, String defaultLabel) {
		for (CustomerCustomField field : customerGroup.getCustomerCustomFields()) {
			if (fieldNumber == field.getCustomField().getIdentification()) {
				return field.getLabel();

			}
		}
		return defaultLabel;
	}

	private String getEquipmentLabelFromCustomField(RegistrationConfig registrationConfig, int fieldNumber, String defaultLabel) {
		switch (fieldNumber) {
			case 1:
				return getValueOrDefault(registrationConfig.getLabelAttribute1(), defaultLabel);
			case 2:
				return getValueOrDefault(registrationConfig.getLabelAttribute2(), defaultLabel);
			case 3:
				return getValueOrDefault(registrationConfig.getLabelAttribute3(), defaultLabel);
			case 4:
				return getValueOrDefault(registrationConfig.getLabelAttribute4(), defaultLabel);
			case 5:
				return getValueOrDefault(registrationConfig.getLabelAttribute5(), defaultLabel);
			case 6:
				return getValueOrDefault(registrationConfig.getLabelAttribute6(), defaultLabel);
			case 7:
				return getValueOrDefault(registrationConfig.getLabelAttribute7(), defaultLabel);
			case 8:
				return getValueOrDefault(registrationConfig.getLabelAttribute8(), defaultLabel);
			default:
				return defaultLabel;
		}
	}
	
	private String getValueOrDefault(String value, String defaultValue) {
		if (StringUtils.hasLength(value)) {
			return value;
		} else {
			return defaultValue;
		}
	}
}
