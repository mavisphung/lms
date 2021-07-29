package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.enties.Question;

public interface QuestionService {

	public List<Question> findAll();

	public Question findById(int theId);

	public void save(Question theQuestion);

	public void deleteById(int theId);
}
