package com.lmsapp.project;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.lmsapp.project.admin.AdminController;
import com.lmsapp.project.role.RoleRepository;
import com.lmsapp.project.user.UserService;

@SpringBootApplication
public class MockLmsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		
		SpringApplication.run(MockLmsApplication.class, args);
	}

	
	// For testing
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
//		roleRepository.saveAll(Arrays.asList(
//				new Role(ERole.ROLE_STUDENT),
//				new Role(ERole.ROLE_ADMIN),
//				new Role(ERole.ROLE_INSTRUCTOR)
//		));
//
//
//		// Testing
//		User newUser = new User(
//				"ntan",
//				"123456",
//				"ntan1902@gmail.com",
//				"1231313213",
//				true,
//				"An",
//				"Trinh"
//		);
//		UserRegistration newUserRegistration = new UserRegistration(
//			newUser,
//			"123456"
//		);
//
//		userService.registerNewUser(
//					newUserRegistration
//		);
	}
}
