package com.lmsapp.project.user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;

@Controller
public class UserController {

	//@Autowired
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String showIndex(Principal principal) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		System.out.println("UserController >> Loaded logged in user: " + user.toString());
		System.out.println("UserController >> Load user roles: " + user.getRoles());		
		return "index";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("registration", new UserRegistration());
		return "user/register";
	}
	
	@GetMapping("/template")
	public String showTemplate() {
		return "template";
	}
	
	@PostMapping("/register")
	public ModelAndView processRegistration(@ModelAttribute("registration") UserRegistration registration, Model model) {
		try {
			User registed = userService.registerNewUser(registration);
		} catch (UserAlreadyExistException ex) {
			ModelAndView mav = new ModelAndView("user/register", "registration", registration);
			mav.addObject("messageError", "Wrong format input");
			return mav;
		}
		return new ModelAndView("login");
	}
	
	@GetMapping("/profile")
	public String showUserProfile(Principal principal, Model model) {
		String username = principal.getName();
		
		//lấy thông tin user
		User user = userService.findByUsername(username);
		List<String> roles = userService.convertToStringList(user.getRoles());
		
		//Tạo đối tượng update
		UserRegistration registration = new UserRegistration();
		registration.setUser(user);
		registration.setRole(roles.get(0));
		model.addAttribute("registration", registration);
		return "profile";
	}
}
