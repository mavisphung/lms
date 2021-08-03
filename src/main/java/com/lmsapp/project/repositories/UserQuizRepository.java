package com.lmsapp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.entities.UserQuizz;

public interface UserQuizRepository extends JpaRepository<UserQuizz, Integer> {
	
	@Query("SELECT uq FROM UserQuizz uq WHERE uq.user.username = ?1")
	List<UserQuizz> getAttemptedQuiz(String username);
	
}
