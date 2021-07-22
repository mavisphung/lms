package com.lmsapp.project.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.enties.Course;
import com.lmsapp.project.repositories.ICourseRepository;
import com.lmsapp.project.services.iservices.ICourseService;



@Service
public class CourseService implements ICourseService {

	private ICourseRepository courseRepository;

	@Autowired
	public CourseService(ICourseRepository theCourseRepository) {
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

}
