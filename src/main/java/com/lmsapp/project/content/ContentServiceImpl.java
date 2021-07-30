package com.lmsapp.project.content;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.enties.Content;
import com.lmsapp.project.enties.Course;
import com.lmsapp.project.enties.Module;

@Service
//@Transactional
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
	public void deleteContentById(int id) {
		repo.deleteById(id);
		
	}

	@Override
	public Content findById(int theId) {
		Optional<Content> result = repo.findById(theId);
		Content content = null;
		if (result.isPresent()) {
			content = result.get();
		} else {
			throw  new RuntimeException("Did not find module id - " + theId);
		}
		return content;
	}

	@Override
	public List<Content> getFiles() {		
		return repo.findAll();
	}

	@Override
	public Content getFile(int fileId) {
		Optional<Content> result = repo.findById(fileId);
		Content con = null;
		if (result.isPresent()) {
			con = result.get();
		} else {
			throw new RuntimeException("Did not find content id - " + fileId);
		}
		return con;
	}
	
		

}
