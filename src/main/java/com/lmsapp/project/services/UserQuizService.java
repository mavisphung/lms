package com.lmsapp.project.services;

import java.util.Map;

import com.lmsapp.project.entities.Quiz;

public interface UserQuizService {
	
	public Map<Quiz, Float> getAttemptedQuiz(String username);
	
}
