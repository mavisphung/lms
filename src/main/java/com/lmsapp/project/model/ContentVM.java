package com.lmsapp.project.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.entities.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentVM {
	private MultipartFile[] files;
	private Module module;
	private List<Content> contents;
}
