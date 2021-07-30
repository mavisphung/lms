package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.enties.Quiz;

public interface QuizService {

	public List<Quiz> findAll();

	public Quiz findById(int theId);

	public void save(Quiz theQuiz);

	public void deleteById(int theId);
}
