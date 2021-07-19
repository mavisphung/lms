package com.lmsapp.project.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.role.Role;


public interface UserRepository extends JpaRepository<User, Long> {
	
	//@Query("SELECT u FROM User u WHERE u.username = ?1")
	User findByUsername(String username);
	
	//@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);
}