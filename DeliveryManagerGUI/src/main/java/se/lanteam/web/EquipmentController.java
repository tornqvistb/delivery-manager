package se.lanteam.web;

import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.model.CorrectionMailInfo;
import se.lanteam.model.ReqOrderLine;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.DeliveryAreaRepository;
import se.lanteam.repository.EquipmentRepository;
import se.lanteam.repository.OrderLineRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.EquipmentValidator;
import se.lanteam.services.MailComposer;

@Controller
public class EquipmentController {

	private static String RESULT_OK = "";
	private static String TOO_MANY_REGISTERED = "Du har angett ett större antal än vad som är kvar att registrera";
	private static String RESULT_CORRECTION_COMPLETED = "Korrigering av utrustning genomförd";

	private OrderRepository orderRepo;
	private OrderLineRepository orderLineRepo;
	private EquipmentRepository equipmentRepo;
	private DeliveryAreaRepository deliveryAreaRepo;
	private EquipmentValidator equipmentValidator;
	private MailComposer mailComposer;
	private SessionBean sessionBean;
	
	private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);
	
	@RequestMapping(value = "order-list/view/registerEquipment/{orderId}", method = RequestMethod.POST)
	public String registerEquipment(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		String valResult = "";
		OrderLine orderLine = orderLineRepo.findOne(reqAttr.getOrderLineId());
		if (orderLine.getHasSerialNo()) {
			Equipment equipment = new Equipment();
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
			valResult = equipmentValidator.validateEquipment(equipment, orderRepo.findOne(orderId));
			if (valResult.equals(RESULT_OK)) {
				orderLine.getEquipments().add(equipment);
				orderLine.setRegistered(orderLine.getRegistered() + 1);
				orderLine.setRemaining(orderLine.getRemaining() - 1);
				orderLineRepo.save(orderLine);
			}
		} else {
			valResult = validateEquipmentNoSN(reqAttr.getTotal(), orderLine);
			if (valResult.equals(RESULT_OK)) {
				orderLine.setRegistered(orderLine.getRegistered() + reqAttr.getTotal());
				orderLine.setRemaining(orderLine.getRemaining() - reqAttr.getTotal());
				orderLineRepo.save(orderLine);
			}
		}
		OrderHeader order = orderRepo.findOne(orderId);
		order.setOrderStatusByProgress();
		orderRepo.save(order);
		model.put("order", order);
		if (RESULT_OK.equals(valResult)) {
			reqAttr = new RequestAttributes();
		}
		reqAttr.setRegEquipmentResult(valResult);
		model.put("reqAttr", reqAttr);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		model.put("deliveryAreas", deliveryAreaRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
		return "order-details";
	}


	@RequestMapping(value = "order-list/view/deleq/{orderId}/{orderLineId}/{equipmentId}", method = RequestMethod.GET)
	public String deleteEquipment(@PathVariable Long orderId, @PathVariable Long orderLineId,
			@PathVariable Long equipmentId, ModelMap model) {
		LOG.debug("equipmentId: " + equipmentId + " ,orderId: " + orderId);
		OrderLine orderLine = orderLineRepo.findOne(orderLineId);
		for (Iterator<Equipment> iterator = orderLine.getEquipments().iterator(); iterator.hasNext();) {			
			Equipment equipment = iterator.next();
			LOG.debug("in loop, equipmentId: " + equipment.getId());
			if (equipment.getId().equals(equipmentId)) {
				LOG.debug("in loop, going to remove equipment: " + equipment.getId());
				equipment.setOrderLine(null);
				iterator.remove();
				orderLine.setRegistered(orderLine.getRegistered() - 1);
				orderLine.setRemaining(orderLine.getRemaining() + 1);
				LOG.debug("Order line updated: " + orderLine.getId());
			}
		}
		orderLineRepo.save(orderLine);
		OrderHeader order = orderRepo.findOne(orderId);
		order.setOrderStatusByProgress();
		orderRepo.save(order);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes(order);
		model.put("reqAttr", reqAttr);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		return "order-details";
	}

	@RequestMapping(value="order-list/correct/confirm/{orderId}", method=RequestMethod.POST)
	public String confirmCorrection(@PathVariable Long orderId, ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		LOG.debug("In confirmCorrection");		
		OrderHeader order = orderRepo.findOne(orderId);
		// Check every modified equipment
		Boolean validationOk = true;
		String result = "";
		String message = "";
		String returnPage = "order-details";
		CorrectionMailInfo mailInfo = new CorrectionMailInfo(order);
		for (ReqOrderLine line : reqAttr.getReqOrderLines()) {			
			for (Equipment equipReq : line.getEquipments()) {
				LOG.debug("equipment: " + equipReq.toString());
				if (equipReq.getId() != null) {
					String valResult = equipmentValidator.validateEquipmentOnCorrection(equipReq, order);
					if (!valResult.equals(RESULT_OK)) {
						validationOk = false;
						result = equipReq.getSerialNo() + " / " + equipReq.getStealingTag() + " - " + valResult;
						break;
					}
				}
			}
		}
		if (validationOk) {
			for (ReqOrderLine line : reqAttr.getReqOrderLines()) {
				for (Equipment equipReq : line.getEquipments()) {
					if (equipReq.getId() != null) {
						Equipment equipDb = equipmentRepo.findOne(equipReq.getId());
						equipDb.setPreviousSerialNo(equipDb.getSerialNo());
						equipDb.setPreviousStealingTag(equipDb.getStealingTag());						
						equipDb.setSerialNo(equipReq.getSerialNo());
						equipDb.setStealingTag(equipReq.getStealingTag());
						equipDb.setRegisteredBy(equipReq.getRegisteredBy());
						equipDb.setToCorrect(false);
						equipmentRepo.save(equipDb);
						mailInfo.getModifiedEquipment().add(equipDb);
					}
				}
			}
			message = RESULT_CORRECTION_COMPLETED;
			mailComposer.createMail(mailInfo);
		} else {
			returnPage = "correct-order";
		}		
		order = orderRepo.findOne(orderId);
		model.put("order", order);
		reqAttr = new RequestAttributes(order);
		reqAttr.setRegEquipmentResult(result);
		reqAttr.setThanksMessage(message);
		model.put("reqAttr", reqAttr);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		return returnPage;
	}

	
	private String validateEquipmentNoSN(Integer count, OrderLine orderLine) {
		String result = RESULT_OK;
		if (count > orderLine.getRemaining()) {
			result = TOO_MANY_REGISTERED;
		}
		return result;
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
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
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
	public void setMailComposer(MailComposer mailComposer) {
		this.mailComposer = mailComposer;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
