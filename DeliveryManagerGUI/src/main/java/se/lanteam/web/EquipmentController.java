package se.lanteam.web;

import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.model.CorrectionMailInfo;
import se.lanteam.model.ReqOrderLine;
import se.lanteam.model.RequestAttributes;
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
	private EquipmentValidator equipmentValidator;
	private MailComposer mailComposer;
	
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
		long orderLineId = reqAttr.getOrderLineId();
		reqAttr = new RequestAttributes();
		reqAttr.setRegEquipmentResult(valResult);
		reqAttr.setOrderLineId(orderLineId);
		model.put("reqAttr", reqAttr);
		return "order-details";
	}


	@RequestMapping(value = "order-list/view/deleq/{orderId}/{orderLineId}/{equipmentId}", method = RequestMethod.GET)
	public String deleteEquipment(@PathVariable Long orderId, @PathVariable Long orderLineId,
			@PathVariable Long equipmentId, ModelMap model) {
		LOG.info("equipmentId: " + equipmentId + " ,orderId: " + orderId);
		OrderLine orderLine = orderLineRepo.findOne(orderLineId);
		for (Iterator<Equipment> iterator = orderLine.getEquipments().iterator(); iterator.hasNext();) {
			Equipment equipment = iterator.next();
			if (equipment.getId() == equipmentId) {
				equipment.setOrderLine(null);
				iterator.remove();
				orderLine.setRegistered(orderLine.getRegistered() - 1);
				orderLine.setRemaining(orderLine.getRemaining() + 1);
			}
		}
		orderLineRepo.save(orderLine);
		OrderHeader order = orderRepo.findOne(orderId);
		order.setOrderStatusByProgress();
		orderRepo.save(order);
		model.put("order", order);
		model.put("reqAttr", new RequestAttributes());
		return "order-details";
	}

	@RequestMapping(value="order-list/correct/confirm/{orderId}", method=RequestMethod.POST)
	public String confirmCorrection(@PathVariable Long orderId, ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		LOG.info("In confirmCorrection");		
		OrderHeader order = orderRepo.findOne(orderId);
		// Check every modified equipment
		Boolean validationOk = true;
		String result = "";
		String message = "";
		String returnPage = "order-details";
		CorrectionMailInfo mailInfo = new CorrectionMailInfo(order);
		for (ReqOrderLine line : reqAttr.getReqOrderLines()) {			
			for (Equipment equipReq : line.getEquipments()) {
				LOG.info("equipment: " + equipReq.toString());
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
}
