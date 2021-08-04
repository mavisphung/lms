package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.entities.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

}
