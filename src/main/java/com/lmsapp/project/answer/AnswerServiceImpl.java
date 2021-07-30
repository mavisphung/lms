package com.lmsapp.project.answer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private AnswerRepository repo;
	@Override
	public void save(Answer answer) {
		repo.save(answer);

	}

	@Override
	public void deleteById(int id) {
		repo.deleteById(id);		

	}

}
