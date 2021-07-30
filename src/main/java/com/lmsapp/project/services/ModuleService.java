package com.lmsapp.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.enties.Module;
import com.lmsapp.project.repositories.IModuleRepository;
import com.lmsapp.project.services.iservices.IModuleService;



@Service
public class ModuleService implements IModuleService {

	private IModuleRepository moduleRepository;

	@Autowired
	public ModuleService(IModuleRepository theModuleRepository) {
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
