package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.entities.Course;

public interface CourseService {
	public List<Course> findAll();

	public Course findById(int theId);

	public void save(Course theCourse);

	public void deleteById(int theId);

	public void setIsActive(int theId);
	
	public List<Course> findCoursesByCourseName(String courseName);
}
