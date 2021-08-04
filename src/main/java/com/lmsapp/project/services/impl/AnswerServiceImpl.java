package com.lmsapp.project.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Answer;
import com.lmsapp.project.repositories.AnswerRepository;
import com.lmsapp.project.services.AnswerService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerServiceImpl implements AnswerService {

	private AnswerRepository answerRepository;

	@Autowired
	public AnswerServiceImpl(AnswerRepository theAnswerRepository) {
		answerRepository = theAnswerRepository;
	}

	@Override
	public List<Answer> findAll() {
		return answerRepository.findAll();
	}

	@Override
	public Answer findById(int theId) {
		Optional<Answer> result = answerRepository.findById(theId);
		Answer theAnswer = null;
		if (result.isPresent()) {
			theAnswer = result.get();
		} else {
			throw new RuntimeException("Did not find answer id - " + theId);
		}
		return theAnswer;
	}

	@Override
	public void save(Answer theAnswer) {
		answerRepository.save(theAnswer);
	}

	@Override
	public void deleteById(int theId) {
		answerRepository.deleteById(theId);
	}

	@Override
	public List<Integer> findAllCorrectAnswer() {
		List<Integer> listCorrectAnswer = new ArrayList<Integer>();
		for (Answer answer : answerRepository.findAll()) {
			if (answer.isCorrect())
				listCorrectAnswer.add(answer.getId());
		}
		return listCorrectAnswer;
	}
}
