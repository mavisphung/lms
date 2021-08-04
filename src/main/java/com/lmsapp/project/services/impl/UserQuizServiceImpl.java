package com.lmsapp.project.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.UserQuizz;
import com.lmsapp.project.repositories.UserQuizRepository;
import com.lmsapp.project.services.UserQuizService;

@Service
public class UserQuizServiceImpl implements UserQuizService {
	
	private UserQuizRepository userQuizRepository;
	
	@Autowired
	public UserQuizServiceImpl(UserQuizRepository userQuizRepository) {
		this.userQuizRepository = userQuizRepository;
	}

	@Override
	public List<UserQuizz> findByUsername(String username) {
		List<UserQuizz> listUserQuiz = userQuizRepository.findByUsername(username);
		if(listUserQuiz.isEmpty() || listUserQuiz.size() <= 0) {
			throw new RuntimeException("You have not attempted any quiz");
		}
		return listUserQuiz;
	}
	
	
}
