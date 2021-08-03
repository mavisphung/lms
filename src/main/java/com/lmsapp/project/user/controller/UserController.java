package com.lmsapp.project.user.controller;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Enrollment;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.EnrollmentService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;
import com.lmsapp.project.util.Utility;

@Controller
public class UserController {

	//@Autowired
	private final UserService userService;
	private final CourseService courseService;
	private final EnrollmentService enrollService;
	
	@Autowired
	public UserController(
			UserService userService,
			CourseService courseService,
			EnrollmentService enrollService) {
		this.userService = userService;
		this.courseService = courseService;
		this.enrollService = enrollService;
	}

	@GetMapping("/")
	public String showIndex(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		
		List<Enrollment> enrollments = enrollService.findByUserId(user.getId());
//		model.addAttribute("courses", enrolledCourses);
		for (Enrollment enrollment : enrollments) {
			System.out.println("UserController >> user " + enrollment.getUser().getUsername() + " enrolled " + enrollment.getCourse().getName());
		}
		model.addAttribute("enrollments", enrollments);
		return "index";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		//data for testing
//		UserRegistration registration = new UserRegistration(new User(), "12345678", "ROLE_STUDENT");
//		registration.getUser().setUsername("huypc2410");
//		registration.getUser().setPassword("12345678");
//		registration.getUser().setAddress("218/25 Hong Bang");
//		registration.getUser().setFirstName("Huy");
//		registration.getUser().setLastName("Phùng");
//		registration.getUser().setEmail("nguoibimatthegioi@gmail.com");
//		model.addAttribute("registration", registration);
		
		
		model.addAttribute("registration", new UserRegistration());
		return "user/register";
	}
	
	@GetMapping("/template")
	public String showTemplate() {
		return "template";
	}
	
	@PostMapping("/register")
	public ModelAndView processRegistration(
			@ModelAttribute("registration") UserRegistration registration, 
			Model model,
			HttpServletRequest request) {
		try {
			User registered = userService.registerNewUser(registration);
			userService.sendVerification(registered, Utility.getSiteUrl(request));
			
		} catch (UserAlreadyExistException ex) {
			ModelAndView mav = new ModelAndView("user/register", "registration", registration);
			mav.addObject("messageError", "User is already existed, try another username");
			return mav;
		} catch (Exception ex) {
			ModelAndView mav = new ModelAndView("user/register", "registration", registration);
			System.out.println(ex.getMessage());
			mav.addObject("messageError", "Username or email is existed!");
			return mav;
		}
		return new ModelAndView("user/confirm-page");
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code) {
		boolean verified = userService.verify(code);
		return verified ? "redirect:/" : "error";
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
	
	@PostMapping("/profile")
	public RedirectView processChangeProfile(@ModelAttribute("registration") UserRegistration registration) {
		RedirectView rv = null;
		try {
			User updated = userService.updateProfile(registration);
		} catch (UserAlreadyExistException ex) {
			System.out.println("UserController >> " + ex.getMessage());
			//ModelAndView mav = new ModelAndView("profile", "registration", registration);
			//mav.addObject("message", ex.getMessage());
			//return mav;
			rv = new RedirectView("/profile");
			rv.addStaticAttribute("registration", registration);
			rv.addStaticAttribute("message", ex.getMessage());
			return rv;
		}
		rv = new RedirectView("/profile");
		rv.addStaticAttribute("registration", registration);
		rv.addStaticAttribute("message", "Your profile has been updated!");
		return rv;
	}
}
