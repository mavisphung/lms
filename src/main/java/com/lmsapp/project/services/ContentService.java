package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.enties.Content;

public interface ContentService {

	public Content save(Content content);
	
	public List<Content> findAll();
	
	public void remove(int id);
}
