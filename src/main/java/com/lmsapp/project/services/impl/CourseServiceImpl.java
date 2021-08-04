package com.lmsapp.project.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.repositories.CourseRepository;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.user.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

	private CourseRepository courseRepository;

	@Autowired
	public CourseServiceImpl(CourseRepository theCourseRepository) {
		courseRepository = theCourseRepository;
	}

	@Override
	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	@Override
	public Course findById(int theId) {
		Optional<Course> result = courseRepository.findById(theId);
		Course theCourse = null;
		if (result.isPresent()) {
			theCourse = result.get();
		} else {
			throw new RuntimeException("Did not find course id - " + theId);
		}
		return theCourse;
	}

	@Override
	public void save(Course theCourse) {
		Date currentDate = new Date();
		theCourse.setCreateDate(currentDate);
		courseRepository.save(theCourse);
	}

	@Override
	public void deleteById(int theId) {
		courseRepository.deleteById(theId);
	}

	@Override
	public void setIsActive(int theId) {
		Course course = findById(theId);
		if(course == null) {
			throw new RuntimeException("Course is not exist");
		}
		if(course.isActive() == false) {
			course.setActive(true);
		}else if(course.isActive() == true) {
			course.setActive(false);
		}
		save(course);
	}

	@Override
	public List<Course> findCoursesByCourseName(String courseName) {

		List<Course> courses = courseRepository.findByCoursenameLike(courseName);

		return courses;
	}

	@Override
	public List<Course> findCourseByUserName(String userName) {
		return courseRepository.findByUserName(userName);
	}

}
