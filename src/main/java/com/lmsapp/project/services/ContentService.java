package com.lmsapp.project.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.entities.Module;

public interface ContentService {

	public Content save(Content content);
	
	public List<Content> findAll();
	
	public void remove(int id);
	
//	Content save(MultipartFile file);
	
	Content save(MultipartFile file, Integer moduleId);
}
