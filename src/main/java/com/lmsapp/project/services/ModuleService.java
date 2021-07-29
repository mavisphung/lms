package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.enties.Module;

public interface ModuleService {

	public List<Module> findAll();

	public Module findById(int theId);
	
	public void save(Module theModule);
	
	public void deleteById(int theId);
}
