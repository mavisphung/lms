package com.lmsapp.project.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.project.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

	@Query("SELECT c FROM Course c WHERE c.name LIKE %?1%")
	List<Course> findByCoursenameLike(String courseName);
	
	@Query("SELECT c FROM Course c WHERE c.username LIKE %?1%")
	List<Course> findByUserName(String userName);
}
