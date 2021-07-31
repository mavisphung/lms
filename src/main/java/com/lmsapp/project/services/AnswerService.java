package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.entities.Answer;

public interface AnswerService {
	public List<Answer> findAll();

	public Answer findById(int theId);

	public void save(Answer theAnswer);

	public void deleteById(int theId);
}
