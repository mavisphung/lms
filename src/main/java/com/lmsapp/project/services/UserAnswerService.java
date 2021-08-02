package com.lmsapp.project.services;

import java.util.List;
import java.util.Map;

import com.lmsapp.project.entities.Question;

public interface UserAnswerService {
	
	Map<Integer, Integer> getUserAnswerList(String username, int quizId, List<Question> listQuestion);
	
}
