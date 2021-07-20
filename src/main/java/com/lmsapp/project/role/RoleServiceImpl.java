package com.lmsapp.project.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private RoleRepository repo;
	
	@Autowired
	public RoleServiceImpl(RoleRepository repo) {
		this.repo = repo;
	}
	
	
	@Override
	public List<Role> findAll() {
		return repo.findAll();
	}
	
}
