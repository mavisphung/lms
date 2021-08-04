package com.lmsapp.project.services;

import java.util.List;
import java.util.Map;

import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.entities.UserQuizz;

public interface UserQuizService {
	
	List<UserQuizz> findByUsername(String username);
	
}
