package com.lmsapp.project.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.repositories.ContentRepository;
import com.lmsapp.project.services.ContentService;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository repo;
	@Override
	public Content save(Content content) {	
		return repo.save(content);
	}

	@Override
	public List<Content> findAll() {		
		return repo.findAll();
	}

	@Override
	public void remove(int id) {
		repo.deleteById(id);
		
	}

}
