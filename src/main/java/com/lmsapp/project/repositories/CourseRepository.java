package com.lmsapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.project.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

}
