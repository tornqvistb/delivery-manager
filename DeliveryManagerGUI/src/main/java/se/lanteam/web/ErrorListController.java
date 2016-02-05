package se.lanteam.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.ErrorRecord;
import se.lanteam.repository.ErrorRepository;

@Controller
public class ErrorListController {
	
	private ErrorRepository errorRepo;
	
	@RequestMapping("error-list")
	public String showErrorList(ModelMap model) {
		List<ErrorRecord> errors = errorRepo.findErrorsByArchived(false);
		model.put("errors", errors);
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr.setErrorStatus(StatusConstants.ERROR_STATUS_NEW);
		model.put("reqAttr", reqAttr);
		return "error-list";
	}
	@RequestMapping(value="error-list/archive/{errorId}", method=RequestMethod.GET)
	public String archiveErrorRecord(@PathVariable Long errorId, ModelMap model) {
		ErrorRecord rec = errorRepo.findOne(errorId);
		rec.setArchived(true);
		errorRepo.save(rec);
		return "redirect:/error-list"; 	
	}
	
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
	@RequestMapping(value="error-list/search", method=RequestMethod.GET)
	public String searchErrors(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		String status = reqAttr.getErrorStatus();
		List<ErrorRecord> errors;
		
		if (status.equals(StatusConstants.ERROR_STATUS_ARCHIVED)){ 
			errors = errorRepo.findErrorsByArchived(true);
		} else {
			errors = errorRepo.findErrorsByArchived(false);
		}		
		model.put("errors", errors);
		model.put("reqAttr", reqAttr);
		return "error-list";
	}
}
