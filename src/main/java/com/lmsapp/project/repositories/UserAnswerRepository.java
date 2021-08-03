package com.lmsapp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.entities.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer>{
	
	@Query("SELECT ua FROM UserAnswer ua WHERE ua.userQuiz.user.username = ?1 AND ua.userQuiz.quiz.id = ?2") 
	public List<UserAnswer> findtByUsernameQuizId(String username, int quizId);
	
}
