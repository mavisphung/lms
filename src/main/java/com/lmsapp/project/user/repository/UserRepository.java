package com.lmsapp.project.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.user.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	//@Query("SELECT u FROM User u WHERE u.username = ?1")
	User findByUsername(String username);
	
	//@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.username LIKE %?1%")
	List<User> findByUsernameLike(String username);
	
	@Query("UPDATE User u SET u.enabled = true WHERE u.id = ?1")
	@Modifying
	void enable(Long id);
	
	User findByVerificationCode(String verificationCode);
}