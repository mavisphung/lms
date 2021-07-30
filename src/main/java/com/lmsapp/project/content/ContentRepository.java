package com.lmsapp.project.content;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.enties.Content;

public interface ContentRepository extends JpaRepository<Content, Integer> {

	//@Query("SELECT new Content(c.id,c.name) FROM content c")
	List<Content> findAll();
}
