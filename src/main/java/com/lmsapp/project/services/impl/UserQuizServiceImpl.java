package com.lmsapp.project.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Quiz;
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
	public Map<Quiz, Float> getAttemptedQuiz(String username){
		List<UserQuizz> listUserQuiz = userQuizRepository.getAttemptedQuiz(username);
		Map<Quiz, Float> listQuiz = new HashMap<Quiz, Float>();
		if(listUserQuiz.size() > 0){
			for (UserQuizz userQuizz : listUserQuiz) {
				Quiz quiz = userQuizz.getQuiz();
				Float score = userQuizz.getScore();
				listQuiz.put(quiz, score);
			}
		} else {
			throw new RuntimeException("You have not attempt any quizz");
		}
		return listQuiz;
	}
}
