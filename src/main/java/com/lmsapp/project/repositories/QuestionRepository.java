package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
