package com.lmsapp.project.services;

import java.util.List;
import java.util.Map;

import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.entities.UserQuizz;

public interface UserQuizService {

	public Map<Quiz, Float> getAttemptedQuiz(String username);

	public void saveAttemptQuiz(UserQuizz userQuizz);

	public UserQuizz findById(int id);
	
	List<UserQuizz> findByUsername(String username);
}
