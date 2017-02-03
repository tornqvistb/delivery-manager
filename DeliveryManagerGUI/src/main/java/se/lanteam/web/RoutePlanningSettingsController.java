package se.lanteam.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.DeliveryArea;
import se.lanteam.domain.DeliveryWeekDay;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.DeliveryAreaRepository;
import se.lanteam.repository.DeliveryWeekDayRepository;

@Controller
public class RoutePlanningSettingsController {
	private static final Logger LOG = LoggerFactory.getLogger(RoutePlanningSettingsController.class);
			
	private DeliveryWeekDayRepository deliveryWeekDayRepo;
	private DeliveryAreaRepository deliveryAreaRepo;
	
	@RequestMapping("route-settings")
	public String showRoutePLanningSettings(ModelMap model) {
		List<DeliveryWeekDay> weekDays = deliveryWeekDayRepo.getAllSorted();
		List<DeliveryArea> areas = deliveryAreaRepo.getAllSorted();
		for (DeliveryWeekDay day : weekDays) {
			List<DeliveryArea> unConnectedAreas = new ArrayList<DeliveryArea>();
			for (DeliveryArea area : areas) {
				if (!day.getAreas().contains(area)) {
					unConnectedAreas.add(area);
				}
			}
			day.setUnConnectedAreas(unConnectedAreas);
		}
		model.put("weekDays", weekDays);
		model.put("areas", areas);
		RequestAttributes reqAttr = new RequestAttributes();
		model.put("reqAttr", reqAttr);
		return "route-settings";
	}
	
	@RequestMapping(value = "route-settings/add/{dayId}/{areaId}", method = RequestMethod.GET)
	public String addAreaToDay(@ModelAttribute RequestAttributes reqAttr,
			 @PathVariable Long dayId, @PathVariable Long areaId,
			ModelMap model) {
		LOG.debug("Adding area to weekday");
		DeliveryArea area = deliveryAreaRepo.findOne(areaId);
		DeliveryWeekDay day = deliveryWeekDayRepo.findOne(dayId);
		day.getAreas().add(area);
		deliveryWeekDayRepo.save(day);
		
		return "redirect:/route-settings";
	}

	@RequestMapping(value = "route-settings/remove/{dayId}/{areaId}", method = RequestMethod.GET)
	public String removeAreaFromDay(@ModelAttribute RequestAttributes reqAttr,
			 @PathVariable Long dayId, @PathVariable Long areaId,
			ModelMap model) {
		LOG.debug("Removing area from weekday");
		DeliveryArea area = deliveryAreaRepo.findOne(areaId);
		DeliveryWeekDay day = deliveryWeekDayRepo.findOne(dayId);
		day.getAreas().remove(area);
		deliveryWeekDayRepo.save(day);
		
		return "redirect:/route-settings";
	}

	
	@Autowired
	public void setDeliveryWeekDayRepo(DeliveryWeekDayRepository deliveryWeekDayRepo) {
		this.deliveryWeekDayRepo = deliveryWeekDayRepo;
	}	
	@Autowired
	public void setDeliveryAreaRepo(DeliveryAreaRepository deliveryAreaRepo) {
		this.deliveryAreaRepo = deliveryAreaRepo;
	}	
}
