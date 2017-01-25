package se.lanteam.web;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.DocumentException;

import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PDFGenerator;


@Controller
public class PDFController {
	
	private OrderRepository orderRepo;
	
	@RequestMapping(value = "generate-report/{orderId}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource createPdfReport(@PathVariable Long orderId, ModelMap model) {
		PDFGenerator gen = new PDFGenerator("pdftemplates/", ".html");
		OrderHeader order = orderRepo.findOne(orderId);
		
		model.put("order", order);

		try {
			gen.generate(new File("/temp/order-report-" + order.getOrderNumber() + ".pdf"), "order-details", model);
			return new FileSystemResource("/temp/order-report-" + order.getOrderNumber() + ".pdf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

}
