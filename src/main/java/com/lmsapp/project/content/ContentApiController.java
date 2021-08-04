package com.lmsapp.project.content;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.services.ContentService;

@RestController
@RequestMapping("/instructor/content/api")
public class ContentApiController {
	
	@Autowired
	private ContentService contentService;
	
	
	@GetMapping("/getAll")
	public ResponseEntity<Object> getAllContents() {
		List<Content> contents = contentService.findAll();
		return new ResponseEntity<Object>(contents, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
		contentService.remove(id);
		return new ResponseEntity<Object>("success", HttpStatus.OK);
	}
}
