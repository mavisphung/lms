package com.lmsapp.project.user.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.hibernate.loader.plan.build.internal.returns.AbstractCompositeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.ERole;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.role.RoleRepository;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public User registerNewUser(UserRegistration registration) throws UserAlreadyExistException {
		User clientUser = registration.getUser();
		//check xem có chứa dấu cách không
		if (clientUser.getUsername().contains(" ")) {
			throw new UserAlreadyExistException("Username can not have space character: " + clientUser.getUsername());
		}
		
		//check trùng username
		if (usernameExists(clientUser.getUsername())) {
			throw new UserAlreadyExistException("There is an account with that username: " + clientUser.getUsername());
		}
		
		//check 2 pass giống nhau
		if (!clientUser.getPassword().equals(registration.getConfirmPassword())) {
			throw new UserAlreadyExistException("The password is not matched.");
		}
		
		
		String encodedPassword = encoder.encode(clientUser.getPassword());
		clientUser.setPassword(encodedPassword);
		clientUser.setEnabled(false);
//		String code = UUID.randomUUID().toString();
		clientUser.setVerificationCode(UUID.randomUUID().toString());
		Role userRole = roleRepo.findByName(convertToERole(registration.getRole()));
		clientUser.getRoles().add(userRole);
		return repo.save(clientUser);
	}
	
	private ERole convertToERole(String rolename) {
		ERole eRole = null;
		switch (rolename) {
			case "ROLE_ADMIN":
				eRole = ERole.ROLE_ADMIN;
				break;
			case "ROLE_INSTRUCTOR":
				eRole = ERole.ROLE_INSTRUCTOR;
				break;
			default:
				eRole = ERole.ROLE_STUDENT;
				break;
		}
		return eRole;
	}
	
	private boolean usernameExists(String username) {
		return repo.findByUsername(username) != null;
	}

	@Override
	public User findByUsername(String username) {
		return repo.findByUsername(username);
	}

	
	@Override
	public List<User> findAll() {
		return repo.findAll();
	}

	@Override
	public User save(User user) {
		return repo.save(user);
	}

	@Override
	public void remove(Long id) {
		repo.deleteById(id);
	}

	@Override
	public void remove(String username) throws RuntimeException {
		User delete = repo.findByUsername(username);
		if (delete == null) {
			throw new RuntimeException("Invalid username");
		}
		repo.delete(delete);
	}
	
	@Override
	public List<User> findUsersByUsername(String username) {
		System.out.println("UserServiceImpl: findUsersByUsername() >> " + username);
		List<User> users = repo.findByUsernameLike(username);
		System.out.println("UserServiceImpl: findUsersByUsername() >> " + users.toString());
		return users;
	}

	@Override
	public List<String> convertToStringList(Set<Role> roles) {
		List<String> strRoles = new ArrayList<String>();
		for (Role role : roles) {
			strRoles.add(role.getName().toString());
		}
		return strRoles;
	}

	/**
	 * This function uses to update profile
	 */
	@Override
	public User updateProfile(UserRegistration registration) throws UserAlreadyExistException {
		User userFromClient = registration.getUser();
		User userFromDb = repo.findByUsername(userFromClient.getUsername());
		
		if (registration.getConfirmPassword() == null || registration.getConfirmPassword().isEmpty()) {
			userFromDb.setFirstName(userFromClient.getFirstName());
			userFromDb.setLastName(userFromClient.getLastName());
			userFromDb.setEmail(userFromClient.getEmail());
			userFromDb.setAddress(userFromClient.getAddress());
		} else {
			
			if (!userFromClient.getPassword().equals(registration.getConfirmPassword())) {
				throw new UserAlreadyExistException("The password is not matched!");
			}
			String encodedPassword = encoder.encode(userFromClient.getPassword());
			userFromDb.setPassword(encodedPassword);
		}
		System.out.println("UserServiceImpl >> Saving profile...");
		return repo.save(userFromDb);
	}
	
	/*
	 * This function is process sending email
	 */
	@Override
	public void sendVerification(User user, String siteUrl) 
			throws UnsupportedEncodingException, MessagingException {
		String subject = "Please verify your registration";
		String senderName = "DoSpringAtHome Team";
		StringBuilder content = new StringBuilder();
		String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
		content.append("<p>Dear, " + user.getFirstName() + " " + user.getLastName() + "</p>")
			   .append("<p>Please click the below link to verify to your registration</p>")
			   .append("<h3><a href=\"" + verifyUrl + "\">VERIFY</a></h3>")
			   .append("<p>Thank you, DoSpringAtHome Team</p>");
		
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("huypc2410@gmail.com", senderName);
		helper.setTo(user.getEmail());
		helper.setSubject(subject);
		helper.setText(content.toString(), true);
		
		mailSender.send(message);
		
	}
	
	public boolean verify(String code) {
		User user = repo.findByVerificationCode(code);
		System.out.println("UserServiceImp: verify(code: String) >> " + user.getUsername());
		
		if (user == null || user.isEnabled()) {
			return false;
		} else {
			repo.enable(user.getId());
			return true;
		}
	}
}
