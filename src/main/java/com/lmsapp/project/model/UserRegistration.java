package com.lmsapp.project.model;

import com.lmsapp.project.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegistration {
	private User user;
	private String confirmPassword;
	private String role;
}
