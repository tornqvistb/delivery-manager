package se.lanteam.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.CustomFieldConstants;
import se.lanteam.constants.RegistrationMethods;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.DeliveryAreaRepository;
import se.lanteam.repository.EquipmentRepository;
import se.lanteam.repository.OrderLineRepository;
import se.lanteam.services.EquipmentValidator;

@Controller
public class EquipmentController extends BaseController {

	private static final String RESULT_OK = "";
	private static final String SERIAL_NUMBER_NOT_ON_THIS_ORDER = "Det serienummer du har angett finns inte plockat på denna order.";
	private OrderLineRepository orderLineRepo;
	private EquipmentRepository equipmentRepo;
	private EquipmentValidator equipmentValidator;
	private SessionBean sessionBean;
	
	private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);
	
	
	@Transactional
	private String updateEquipment(RequestAttributes reqAttr, ModelMap model) {
		String valResult = RESULT_OK;
		Long orderId = reqAttr.getOrderHeaderId();
		Equipment equipment = getEquipmentFromCurrentOrder(orderId, reqAttr.getSerialNo());
		if (equipment != null) {
			OrderLine orderLine = orderLineRepo.findOne(equipment.getOrderLine().getId());
			equipment.setCreationDate(new Date());
			equipment.setOrderLine(orderLine);
			equipment.setSerialNo(reqAttr.getSerialNo());
			equipment.setStealingTag(reqAttr.getStealingTag());
			equipment.setRegisteredBy(reqAttr.getRegisteredBy());
			RegistrationConfig regConfig = sessionBean.getCustomerGroup().getRegistrationConfig();
			if (regConfig.getUseAttribute1()) {
				equipment.setCustomAttribute1(reqAttr.getCustomAttribute1());
				equipment.setCustomAttribute1Label(regConfig.getLabelAttribute1());
			}
			if (regConfig.getUseAttribute2()) {
				equipment.setCustomAttribute2(reqAttr.getCustomAttribute2());
				equipment.setCustomAttribute2Label(regConfig.getLabelAttribute2());
			}
			if (regConfig.getUseAttribute3()) {
				equipment.setCustomAttribute3(reqAttr.getCustomAttribute3());
				equipment.setCustomAttribute3Label(regConfig.getLabelAttribute3());
			}
			if (regConfig.getUseAttribute4()) {
				equipment.setCustomAttribute4(reqAttr.getCustomAttribute4());
				equipment.setCustomAttribute4Label(regConfig.getLabelAttribute4());
			}
			if (regConfig.getUseAttribute5()) {
				equipment.setCustomAttribute5(reqAttr.getCustomAttribute5());
				equipment.setCustomAttribute5Label(regConfig.getLabelAttribute5());
			}
			if (regConfig.getUseAttribute6()) {
				equipment.setCustomAttribute6(reqAttr.getCustomAttribute6());
				equipment.setCustomAttribute6Label(regConfig.getLabelAttribute6());
			}
			if (regConfig.getUseAttribute7()) {
				equipment.setCustomAttribute7(reqAttr.getCustomAttribute7());
				equipment.setCustomAttribute7Label(regConfig.getLabelAttribute7());
			}
			if (regConfig.getUseAttribute8()) {
				equipment.setCustomAttribute8(reqAttr.getCustomAttribute8());
				equipment.setCustomAttribute8Label(regConfig.getLabelAttribute8());
			}
			valResult = equipmentValidator.validateEquipment(equipment, orderRepo.findOne(orderId), orderLine.getRestrictionCode(), reqAttr.isUpdateEquipment());
			if (valResult.equals(RESULT_OK)) {
				orderLine.getEquipments().add(equipment);
				orderLine.updateEquipmentCounters();
				orderLineRepo.save(orderLine);				 
			}
		} else {
			valResult = SERIAL_NUMBER_NOT_ON_THIS_ORDER;
		}
		OrderHeader order = orderRepo.findOne(orderId);
		
		boolean workToDoOnRelatedOrders = workToDoOnRelatedOrders(order);
			
		if (RESULT_OK.equals(valResult)) {
			order.setOrderStatusByProgress(workToDoOnRelatedOrders);
			orderRepo.save(order);
			updateRelatedOrders(order, workToDoOnRelatedOrders);
			reqAttr = new RequestAttributes();
			// Om uppdatering av utrustning kvarstår, hitta nästa om moteoden är DEFAULT.
			if (RegistrationMethods.DEFAULT == order.getRegistrationMethod()) {
				Equipment eq = getNextEquipmentToUpdate(order);
				if (eq != null) {
					reqAttr.setOrderLineIdToUpdate(eq.getOrderLine().getId());
					reqAttr.setSerialNoToUpdate(eq.getSerialNo());
				}			
			}
		}
		model.put("order", order);
		reqAttr.setUpdateEquipmentResult(valResult);
		reqAttr = addRelatedOrders(reqAttr, order);
		setInfoMessageForRelatedOrders(reqAttr, order, workToDoOnRelatedOrders);
		
		model.put("reqAttr", reqAttr);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		model.put("deliveryAreas", deliveryAreaRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
		return "order-details";
	}
		
	private Equipment getEquipmentFromCurrentOrder (Long orderId, String serialNo) {
		List<Equipment> eqs = equipmentRepo.findBySerialNo(serialNo);
		if (!eqs.isEmpty()) {
			OrderLine orderLine = eqs.get(0).getOrderLine();
			if (orderLine.getOrderHeader().getId().equals(orderId)) {
				return eqs.get(0);
			}
		}
		return null;
	}
	
	private Equipment getNextEquipmentToUpdate(OrderHeader order) {
		for (OrderLine ol : order.getOrderLines()) {
			for (Equipment eq : ol.getEquipments()) {
				if (StringUtils.isEmpty(eq.getStealingTag())) {
					return eq;
				}
			}
		}
		return null;
	}

	private boolean workToDoOnRelatedOrders(OrderHeader order) {
		boolean result = false;
		List<OrderHeader> relatedOrders = getRelatedOrders(order);
		for (OrderHeader relatedOrder : relatedOrders) {
			if (!relatedOrder.getUnCompletedOrderLines().isEmpty()) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private void updateRelatedOrders(OrderHeader order, boolean updateRelatedOrders) {
		if (order.getUnCompletedOrderLines().size() == 0 && !updateRelatedOrders) {
			// Current orderline finished and also related order lines			
			List<OrderHeader> relatedOrders = getRelatedOrders(order);
			for (OrderHeader relatedOrder : relatedOrders) {
				relatedOrder.setOrderStatusByProgress(false);
				orderRepo.save(relatedOrder);
			}
		}
	}

	private RequestAttributes setInfoMessageForRelatedOrders(RequestAttributes reqAttr, OrderHeader order, boolean workToDoOnRelatedOrders) {
		if (!order.getUnCompletedOrderLines().isEmpty() && workToDoOnRelatedOrders) {
			reqAttr.setInfoMessage(CustomFieldConstants.TEXT_SAMLEVERANS);
		}
		return reqAttr;
	}
		
	@Transactional
	@RequestMapping(value = "order-list/view/updateEquipment/{orderId}", method = RequestMethod.POST)
	public String updateEquipment(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		LOG.info("Uppdatering - serienummer: " + reqAttr.getSerialNo());
		reqAttr.setOrderHeaderId(orderId);
		return updateEquipment(reqAttr, model);
	}
	
	@Autowired
	public void setDeliveryAreaRepo(DeliveryAreaRepository deliveryAreaRepo) {
		this.deliveryAreaRepo = deliveryAreaRepo;
	}
	@Autowired
	public void setEquipmentRepo(EquipmentRepository equipmentRepo) {
		this.equipmentRepo = equipmentRepo;
	}
	@Autowired
	public void setOrderLineRepo(OrderLineRepository orderLineRepo) {
		this.orderLineRepo = orderLineRepo;
	}
	@Autowired
	public void setEquipmentValidator(EquipmentValidator equipmentValidator) {
		this.equipmentValidator = equipmentValidator;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
