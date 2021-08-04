package com.lmsapp.project.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.UserAnswer;
import com.lmsapp.project.repositories.UserAnswerRepository;
import com.lmsapp.project.services.UserAnswerService;

@Service
public class UserAnswerServiceImpl implements UserAnswerService{
	
	private UserAnswerRepository userAnswerRepository;
	
	@Autowired
	public UserAnswerServiceImpl (UserAnswerRepository userAnswerRepository) {
		this.userAnswerRepository=userAnswerRepository;
	}

	@Override
	public List<UserAnswer> findByUserQuizId(int userQuizId) {
		List<UserAnswer> result = userAnswerRepository.findtByUserQuizId(userQuizId);
		if(result.isEmpty() || result.size() <= 0) {
			throw new RuntimeException("Can't find this quiz");
		}
		return result;
	}
}
