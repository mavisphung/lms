package com.lmsapp.project.user;

import com.lmsapp.project.model.UserRegistration;

public interface UserService {
	
	User registerNewUser(UserRegistration registration);
	
}
