package com.lmsapp.project.content;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.enties.Content;


public interface ContentService {
	
	public List<Content> getFiles();
	
	public Content getFile(int fileId);
	
	public Content save(Content content);
	
	public List<Content> findAll();
	
	public Content findById(int theId);
	
	public void deleteContentById(int id);
	
	
}
