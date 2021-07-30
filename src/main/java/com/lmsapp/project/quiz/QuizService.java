package com.lmsapp.project.quiz;

import java.util.List;


public interface QuizService{

	public List<Quiz> findAll();
		
	public Quiz findById(int id);
	
	public void save(Quiz quiz);
	
	public void deleteById(int id);
	
}
