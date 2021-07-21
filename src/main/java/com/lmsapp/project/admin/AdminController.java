package com.lmsapp.project.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.role.RoleService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;
	private RoleService roleService;
	
	@Autowired
	public AdminController(
			UserService userService, 
			RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping("/")
	public String showAdminIndex(Model model) {
		List<User> users = userService.findAll();
		callGetRoles(users);
		model.addAttribute("users", users);
		return "admin/index";
	}
	
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		model.addAttribute("registration", new UserRegistration());
		return "admin/create-new-user";
	}
	
	@PostMapping("/create")
	public String processCreateUser(
			@ModelAttribute("registration") UserRegistration registration,
			Model model) {
		String url = "redirect:/admin/create";
		System.out.println("AdminController: /admin/create POST >> Created " + registration.getUser().toString());
		System.out.println("AdminController: /admin/create POST >> Created user role " + registration.getRole());
		System.out.println("AdminController: /admin/create POST >> Created confirm password " + registration.getConfirmPassword());
		
		try {
			User registered = userService.registerNewUser(registration);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}
		
		return url;
	}
	
	@PostMapping("/delete")
	public String processDelete(@RequestParam("username") String username) {
		System.out.println("AdminController: /admin/delete/ POST >> Deleting user " + username);
		String url = "redirect:/admin/";
		userService.remove(username);
		return url;
	}
	
	@PostMapping("/enable")
	public String showEditPage(
			@RequestParam("username") String username,
			Model model) {
		System.out.println("AdminController: /admin/edit/ GET >> Retrieving user " + username);
		String url = "redirect:/admin/";
		try {
			User userFromDb = userService.findByUsername(username);
			if (userFromDb.isEnabled()) {
				userFromDb.setEnabled(false);
				System.out.println("Username " + userFromDb.getUsername() + " is locked");
			} else {
				userFromDb.setEnabled(true);
				System.out.println("Username " + userFromDb.getUsername() + " is unlocked");
			}
			userService.save(userFromDb);
			
		} catch (Exception e) {
			System.err.println("AdminController: /admin/enable/ POST >> " + e.getMessage());
			model.addAttribute("error", "Invalid username");
		}
		
		return url;
	}
	
	@GetMapping("/search")
	public String searchValue(
			@RequestParam("searchValue") String searchValue,
			Model model) {
		List<User> users = userService.findUsersByUsername(searchValue);
		callGetRoles(users);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("users", users);
		System.out.println("AdminController: /admin/search GET >> " + users.toString());
		System.out.println("AdminController: /admin/search GET >> Search value: " + searchValue.trim());
		
		return "admin/index";
	}
	
	private void callGetRoles(List<User> users) {
		for (User user : users) {
			Set<Role> userRoles = user.getRoles();
			List<String> strRoles = convertToStringList(userRoles);
			user.setStrRoles(strRoles);
		}
	}
	
	//API CALLS
//	@GetMapping("/users")
//	public List<User> getAllUsers() {
//		List<User> users = userService.findAll();
//		return users;
//	}
	
	
	private List<String> convertToStringList(Set<Role> roles) {
		List<String> strRoles = new ArrayList<String>();
		for (Role role : roles) {
			strRoles.add(role.getName().toString());
		}
		return strRoles;
	}
	
}
