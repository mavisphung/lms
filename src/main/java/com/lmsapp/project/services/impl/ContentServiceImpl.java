package com.lmsapp.project.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.entities.Module;
import com.lmsapp.project.model.ContentVM;
import com.lmsapp.project.repositories.ContentRepository;
import com.lmsapp.project.repositories.ModuleRepository;
import com.lmsapp.project.services.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository repo;
	
	@Autowired
	private ModuleRepository moduleRepo;
	
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
	public Content save(MultipartFile file, Module module) {
		try {
			Content content = new Content(
					file.getOriginalFilename(), 
					file.getContentType(), 
					file.getBytes()
				);
			content.setModule(module);
			return repo.save(content);
		} catch (Exception e) {
			System.out.println("ContentServiceImpl: Error save(MultipartFile) >> " + e.getMessage());
		}
		return null;
	}

	@Override
	public Content save(MultipartFile file) {
//		Module module = vm.getModule();
//		MultipartFile[] files = vm.getFiles();
//		try {
//			for (MultipartFile file : files) {
//				Content content = new Content(
//						file.getOriginalFilename(),
//						file.getContentType(),
//						file.getBytes()
//					);
//				content.setModule(module);
//				repo.save(content);
//			}
//		} catch (Exception e) {
//			System.out.println("ContentServiceImpl: Error while saving >> \n" + e.getMessage());
//			return false;
//		}
//		return true;
		return null;
	}
	
	
}
