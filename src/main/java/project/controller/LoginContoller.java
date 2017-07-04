package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;

import project.util.SecurityUtil;


@Controller
public class LoginContoller {

	@GetMapping("/login")
	public String login() {
		// check if user already login and redirect him to home page if so
        if (SecurityUtil.getCurrentLogin() != null) {
        	return "redirect:/";
        } 
        return "login";
	}
	
	@GetMapping("/")
	public String home() {
        return "mainPage";
	}
	
	@GetMapping("/error/{code}")
	public String error(@PathVariable("code") Integer code, Model model) {
		model.addAttribute("errorCode", code + ".jpg");
        return "error";
	}
	
	@GetMapping("/badToken")
	public String badToken() {
        return "confirmation_error";
	}

}
