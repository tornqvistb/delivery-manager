package se.lanteam.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.SystemProperty;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.PropertyRepository;

@Controller
public class SettingsController {
	private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);
	private PropertyRepository propertyRepo;
			
	@RequestMapping("settings")
	public String showSettings(ModelMap model) {
		List<SystemProperty> properties = propertyRepo.findAll();
		model.put("properties", properties);
		RequestAttributes reqAttr = loadReqAttr(properties);
		model.put("reqAttr", reqAttr);
		return "settings";
	}
	
	@RequestMapping(value = "settings/save", method = RequestMethod.POST)
	public String saveSettings(@ModelAttribute RequestAttributes reqAttr,
			ModelMap model) {
		LOG.debug("Saving properties");
		for (SystemProperty prop : reqAttr.getSystemProperties()) {
			SystemProperty theProp = propertyRepo.getOne(prop.getId());
			theProp.setNumberValue(prop.getNumberValue());
			theProp.setStringValue(prop.getStringValue());
			propertyRepo.save(theProp);
		}
		List<SystemProperty> properties = propertyRepo.findAll();
		model.put("properties", properties);
		reqAttr = loadReqAttr(properties);
		reqAttr.setStatusMessageCreationSuccess("Uppdatering genomförd");
		model.put("reqAttr", reqAttr);
		return "settings";
	}

	private RequestAttributes loadReqAttr(List<SystemProperty> properties) {
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr.setSystemProperties(properties);
		return reqAttr;
	}
	
	@Autowired
	public void setPropertyRepo(PropertyRepository propertyRepo) {
		this.propertyRepo = propertyRepo;
	}	
}
