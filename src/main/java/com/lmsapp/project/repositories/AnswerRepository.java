package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
