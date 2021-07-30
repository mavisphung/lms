package com.lmsapp.project.question;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository repo;
	@Override
	public void save(Question question) {
		repo.save(question);

	}

	@Override
	public void deleteById(int id) {
		repo.deleteById(id);
	}

}
