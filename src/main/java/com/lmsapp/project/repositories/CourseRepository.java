package com.lmsapp.project.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsapp.project.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

	
	@Query("SELECT c FROM Course c WHERE c.name LIKE %:coursename% AND c.username LIKE :username")
	List<Course> findByCoursenameLike(@Param("coursename") String coursename,@Param("username") String username);
	
	@Query("SELECT c FROM Course c WHERE c.username LIKE :username")
	List<Course> findByUserName(@Param("username") String username);
}
