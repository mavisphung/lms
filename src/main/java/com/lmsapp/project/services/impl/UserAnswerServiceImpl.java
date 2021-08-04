package com.lmsapp.project.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Question;
import com.lmsapp.project.entities.UserAnswer;
import com.lmsapp.project.repositories.UserAnswerRepository;
import com.lmsapp.project.services.UserAnswerService;

@Service
public class UserAnswerServiceImpl implements UserAnswerService{
	
	private UserAnswerRepository userAnswerRepository;
	
	@Autowired
	public UserAnswerServiceImpl (UserAnswerRepository userAnswerRepository) {
		this.userAnswerRepository = userAnswerRepository;
	}

	@Override
	public Map<Integer, Integer> getUserAnswerList(String username, int quizId, List<Question> listQuestion) {
		Map<Integer, Integer> listUserAnswer = new HashMap<Integer, Integer>();
		int questionId;
		int answerId;
		for (Question question : listQuestion) {
			questionId = question.getId();
			answerId = userAnswerRepository.getUserAnswer(username, quizId, questionId);
			listUserAnswer.put(questionId, answerId);
		}
		return listUserAnswer;
	}

	@Override
	public void save(UserAnswer userAnwser) {
		userAnswerRepository.save(userAnwser);
	}
	
	@Override
	public List<UserAnswer> findByUsernameQuiz(String username, int quizId) {
		List<UserAnswer> result = userAnswerRepository.findtByUsernameQuizId(username, quizId);
		if(result.isEmpty() || result.size() <= 0) {
			throw new RuntimeException("Can't find this quiz");
		}
		return result;
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
