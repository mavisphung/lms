package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.enties.Module;

public interface IModuleRepository extends JpaRepository<Module, Integer> {

}
