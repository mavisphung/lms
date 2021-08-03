package com.lmsapp.project.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Course;
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

	@Override
	public void saveAttemptQuiz(UserQuizz userQuizz) {
		userQuizRepository.save(userQuizz);
	}

	@Override
	public UserQuizz findById(int id) {
		Optional<UserQuizz> result = userQuizRepository.findById(id);
		UserQuizz userQuiz = null;
		if (result.isPresent()) {
			userQuiz = result.get();
		} else {
			throw new RuntimeException("Did not find userquiz id - " + id);
		}
		return userQuiz;
	}
}
