package com.lmsapp.project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.repositories.QuizRepository;
import com.lmsapp.project.services.QuizService;

@Service
public class QuizServiceImpl implements QuizService {

	private QuizRepository quizRepository;

	@Autowired
	public QuizServiceImpl(QuizRepository theQuizRepository) {
		quizRepository = theQuizRepository;
	}

	@Override
	public List<Quiz> findAll() {
		return quizRepository.findAll();
	}

	@Override
	public Quiz findById(int theId) {
		Optional<Quiz> result = quizRepository.findById(theId);
		Quiz theQuiz = null;
		if (result.isPresent()) {
			theQuiz = result.get();
		} else {
			throw new RuntimeException("Did not find quiz id - " + theId);
		}
		return theQuiz;
	}

	@Override
	public void save(Quiz theQuiz) {
		quizRepository.save(theQuiz);
	}

	@Override
	public void deleteById(int theId) {
		quizRepository.deleteById(theId);
	}
}
