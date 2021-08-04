package com.lmsapp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.entities.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer>{
	
	@Query("SELECT ua FROM UserAnswer ua WHERE ua.userQuiz.id = ?1") 
	public List<UserAnswer> findtByUserQuizId(int userQuizId);
	
}
