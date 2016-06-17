package se.lanteam.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.Equipment;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.EquipmentRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class SearchController {
	
	private OrderRepository orderRepo;
	private EquipmentRepository equipmentRepo;
	private ErrorRepository errorRepo;
	
	@RequestMapping("search-equipment")
	public String showOrderList(ModelMap model) {
		model.put("reqAttr", new RequestAttributes(errorRepo.findErrorsByArchived(false).size()));
		return "search-equipment";
	}
	@RequestMapping(value="search-equipment/do-search", method=RequestMethod.GET)
	public String searchOrders(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		String errorMessage = "";
		String query = reqAttr.getQuery();
		if (!StringUtils.isEmpty(query)) {
			List<Equipment> equipments = equipmentRepo.findBySearchQuery("%" + query + "%");
					
			model.put("equipments", equipments);
			
			if (equipments.isEmpty()) {
				// If no hits create error message in reqattr
				errorMessage = "Sökningen på \"" + query + "\" gav inga träffar.";
			}
		} else {
			errorMessage = "Du måste ange en söksträng vid sökning.";
		}
		reqAttr = new RequestAttributes(errorRepo.findErrorsByArchived(false).size());
		reqAttr.setQuery(query);
		reqAttr.setErrorMessage(errorMessage);
		model.put("reqAttr", reqAttr);
		return "search-equipment";
	}
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
	@Autowired
	public void setEquipmentRepo(EquipmentRepository equipmentRepo) {
		this.equipmentRepo = equipmentRepo;
	}
	
}
