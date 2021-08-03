package com.lmsapp.project.services;

import java.util.List;
import java.util.Map;

import com.lmsapp.project.entities.Question;
import com.lmsapp.project.entities.UserAnswer;

public interface UserAnswerService {
	
	Map<Integer, Integer> getUserAnswerList(String username, int quizId, List<Question> listQuestion);
	
	public void save(UserAnswer userAnwser);
}
