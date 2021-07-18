package com.lmsapp.project.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	
	@GetMapping("/")
	public String showIndex() {
		return "index";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegister() {
		return "register";
	}
	
	@GetMapping("/template")
	public String showTemplate() {
		return "template";
	}
}
