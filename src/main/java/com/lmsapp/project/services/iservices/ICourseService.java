package com.lmsapp.project.services.iservices;

import java.util.List;

import com.lmsapp.project.enties.Course;



public interface ICourseService {
	public List<Course> findAll();

	public Course findById(int theId);
	
	public void save(Course theCourse);
	
	public void deleteById(int theId);
}
