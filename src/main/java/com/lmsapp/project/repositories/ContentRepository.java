package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.enties.Content;

public interface ContentRepository extends JpaRepository<Content, Integer> {

}
