package com.lmsapp.project.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmsapp.project.user.CustomUserDetails;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByUsername(username);
		if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
		System.out.println("loadUserByUsername(String): Load username = " + user.getUsername());
		return new CustomUserDetails(user);
	}

}
