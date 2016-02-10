package se.lanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.domain.SystemProperty;
import se.lanteam.repository.PropertyRepository;

@Service
public class PropertyService {

	@Autowired
	private PropertyRepository propertyRepo;
	
	public String getString(String propertyId) {
		String result = "";
		SystemProperty prop = propertyRepo.findOne(propertyId);
		if (prop != null) {
			result = prop.getStringValue();
		}
		return result;
	}
	
	public Long getLong(String propertyId) {
		Long result = 0L;
		SystemProperty prop = propertyRepo.findOne(propertyId);
		if (prop != null) {
			result = prop.getNumberValue();
		}
		return result;		
	}
	
}
