package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.entities.UserAnswer;

public interface UserAnswerService {
	
	public List<UserAnswer> findByUsernameQuiz(String username, int quizId);
	
}
