package com.lmsapp.project.services;

import java.util.List;

import com.lmsapp.project.entities.Enrollment;

public interface EnrollmentService {
	
	List<Enrollment> findByUserId(Long userId);
	
	Enrollment save(Enrollment enrollment);
	
	Enrollment findByUsernameAndCourseId(String username, Integer courseId);
}
