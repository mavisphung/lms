package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.enties.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

}
