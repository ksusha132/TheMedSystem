package project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;


@ControllerAdvice
@Component
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public RedirectView handleResourceNotFoundException(ConstraintViolationException e, 
			HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		
		System.out.println("handle exeption");
		String referer = request.getHeader("Referer");
    	String redirectUrl = referer.substring(0, 
    			referer.indexOf("?") != -1 ? referer.indexOf("?") : referer.length());
		RedirectView redirectView = new RedirectView(redirectUrl, false);
		
		FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
	    if (outputFlashMap != null){
	    	for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
	    		outputFlashMap.put("errors", violation.getMessage()); 
	    	}
	        
	    }

	    return redirectView;
	}
}
