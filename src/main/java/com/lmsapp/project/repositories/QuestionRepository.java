package com.lmsapp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

	@Query("SELECT q FROM Question q WHERE q.quiz.id = ?1")
	List<Question> findByQuizId(int quizId);
	
}
