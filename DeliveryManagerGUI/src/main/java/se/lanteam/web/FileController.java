package se.lanteam.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import se.lanteam.domain.Attachment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.OrderRepository;

@Controller
public class FileController extends BaseController {

	private static final String ATTACHMENT_MSG_OK = "Leveransdokument bifogat.";
	private static final String ATTACHMENT_MSG_DELETE_OK = "Leveransdokument borttaget.";
	private static final String ATTACHMENT_MSG_FILE_MISSING = "Du måste välja en fil.";
	private static final String ATTACHMENT_MSG_FAILED = "Det gick inte att bifoga dokumentet. ";

	@RequestMapping(value = "order-list/view/attachFile/{orderId}", method = RequestMethod.POST)
	public String attachFile(@RequestParam("attachment") MultipartFile attachment, @PathVariable Long orderId,
			ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		RequestAttributes reqAttr = new RequestAttributes();
		if (!attachment.isEmpty()) {
			try {
				Attachment attEntity = new Attachment();
				attEntity.setOrderHeader(order);
				attEntity.setFileContent(attachment.getBytes());
				attEntity.setFileName(attachment.getOriginalFilename());
				attEntity.setFileSize(attachment.getSize());
				attEntity.setContentType("image/jpeg");
				order.setAttachment(attEntity);
				order.setOrderStatusByProgress(false);
				orderRepo.save(order);
				reqAttr.setStatusAttachmentSuccess(ATTACHMENT_MSG_OK);
			} catch (Exception e) {
				reqAttr.setStatusAttachmentFailed(ATTACHMENT_MSG_FAILED + e.getMessage());
			}
		} else {
			reqAttr.setStatusAttachmentFailed(ATTACHMENT_MSG_FILE_MISSING);
		}
		order = orderRepo.findOne(orderId);
		model.put("order", order);
		reqAttr = addRelatedOrders(reqAttr, order);
		model.put("reqAttr", reqAttr);
		return "order-details";
	}

	@RequestMapping(value = "order-list/view/delfile/{orderId}", method = RequestMethod.GET)
	public String deleteFile(@PathVariable Long orderId, ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		order.setAttachment(null);
		order.setOrderStatusByProgress(false);
		orderRepo.save(order);
		order = orderRepo.findOne(orderId);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr.setStatusAttachmentSuccess(ATTACHMENT_MSG_DELETE_OK);
		reqAttr = addRelatedOrders(reqAttr, order);
		model.put("reqAttr", reqAttr);
		return "order-details";
	}

	/*
	@RequestMapping(value="order-list/view/viewfile/{orderId}", method=RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFile(
			@PathVariable Long orderId) throws IOException {

		OrderHeader order = orderRepo.findOne(orderId);
	    byte[] documentBody = order.getAttachment().getFileContent();

	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.IMAGE_JPEG);
	    return new ResponseEntity<byte[]>(documentBody, header, HttpStatus.CREATED);
	}
	*/
	// If different file types in attachments, maybe use method above instead
	@ResponseBody
	@RequestMapping(value="order-list/view/viewfile/{orderId}", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] downloadFile(
			@PathVariable Long orderId) throws IOException {
		OrderHeader order = orderRepo.findOne(orderId);
		return order.getAttachment().getFileContent();
	}
}
