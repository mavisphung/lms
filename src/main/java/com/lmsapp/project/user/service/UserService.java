package com.lmsapp.project.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.user.User;

public interface UserService {
	
	User registerNewUser(UserRegistration registration);
	
	User findByUsername(String username);
	
	List<User> findAll();
	
	User save(User user);
	
	void remove(Long id);
	
	void remove(String username);
	
	List<User> findUsersByUsername(String username);
	
	List<String> convertToStringList(Set<Role> roles);
	
	User updateProfile(UserRegistration registration);
	
	void sendVerification(User user, String siteUrl) 
			throws UnsupportedEncodingException, MessagingException;
	
	boolean verify(String code);
}
