package com.lmsapp.project.user;

import java.util.List;

import com.lmsapp.project.model.UserRegistration;

public interface UserService {
	
	User registerNewUser(UserRegistration registration);
	
	User findByUsername(String username);
	
	List<User> findAll();
	
	User save(User user);
	
	void remove(Long id);
	
	void remove(String username);
	
	List<User> findUsersByUsername(String username);
}
