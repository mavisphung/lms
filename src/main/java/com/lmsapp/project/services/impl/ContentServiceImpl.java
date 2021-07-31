package com.lmsapp.project.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.entities.Module;
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

	@Override
	public Content save(MultipartFile file, Integer moduleId) {
		try {
			Content content = new Content(
					file.getOriginalFilename(), 
					file.getContentType(), 
					file.getBytes()
				);
			return repo.save(content);
		} catch (Exception e) {
			System.out.println("ContentServiceImpl: Error save(MultipartFile) >> " + e.getMessage());
		}
		return null;
	}
}
