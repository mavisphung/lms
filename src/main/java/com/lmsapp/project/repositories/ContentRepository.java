package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.entities.Content;

public interface ContentRepository extends JpaRepository<Content, Integer> {

}
