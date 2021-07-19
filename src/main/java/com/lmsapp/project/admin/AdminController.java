package com.lmsapp.project.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String showAdminIndex(Model model) {
		List<User> users = userService.findAll();

		for (User user : users) {
			Set<Role> userRoles = user.getRoles();
			List<String> strRoles = convertToString(userRoles);
			user.setStrRoles(strRoles);
		}

		model.addAttribute("users", users);
		return "admin/index";
	}
	
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		model.addAttribute("registration", new UserRegistration());
		return "admin/create-new-user";
	}
	
	
	private List<String> convertToString(Set<Role> roles) {
		List<String> strRoles = new ArrayList<String>();
		for (Role role : roles) {
			strRoles.add(role.getName().toString());
		}
		return strRoles;
	}
}
