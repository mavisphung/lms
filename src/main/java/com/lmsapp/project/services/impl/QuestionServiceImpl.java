package com.lmsapp.project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Question;
import com.lmsapp.project.repositories.QuestionRepository;
import com.lmsapp.project.services.QuestionService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionServiceImpl implements QuestionService {

	private QuestionRepository questionRepository;

	@Autowired
	public QuestionServiceImpl(QuestionRepository theQuestionRepository) {
		questionRepository = theQuestionRepository;
	}

	@Override
	public List<Question> findAll() {
		return questionRepository.findAll();
	}

	@Override
	public Question findById(int theId) {
		Optional<Question> result = questionRepository.findById(theId);
		Question theQuestion = null;
		if (result.isPresent()) {
			theQuestion = result.get();
		} else {
			throw new RuntimeException("Did not find question id - " + theId);
		}
		return theQuestion;
	}

	@Override
	public void save(Question theQuestion) {
		questionRepository.save(theQuestion);
	}

	@Override
	public void deleteById(int theId) {
		questionRepository.deleteById(theId);
	}
}
