package com.lmsapp.project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Module;
import com.lmsapp.project.repositories.ModuleRepository;
import com.lmsapp.project.services.ModuleService;



@Service
public class ModuleServiceImpl implements ModuleService {

	private ModuleRepository moduleRepository;

	@Autowired
	public ModuleServiceImpl(ModuleRepository theModuleRepository) {
		moduleRepository = theModuleRepository;
	}

	@Override
	public List<Module> findAll() {
		return moduleRepository.findAll();
	}

	@Override
	public Module findById(int theId) {
		Optional<Module> result = moduleRepository.findById(theId);
		Module theModule = null;
		if (result.isPresent()) {
			theModule = result.get();
		} else {
			throw  new RuntimeException("Did not find module id - " + theId);
		}
		return theModule;
	}

	@Override
	public void save(Module theModule) {
		moduleRepository.save(theModule);
	}

	@Override
	public void deleteById(int theId) {
		moduleRepository.deleteById(theId);
	}

}
