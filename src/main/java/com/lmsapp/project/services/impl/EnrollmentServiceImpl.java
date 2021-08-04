package com.lmsapp.project.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsapp.project.entities.Enrollment;
import com.lmsapp.project.repositories.EnrollmentRepository;
import com.lmsapp.project.services.EnrollmentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
	
	private EnrollmentRepository enrollRepo;
	
	@Autowired
	public EnrollmentServiceImpl(EnrollmentRepository enrollRepo) {
		this.enrollRepo = enrollRepo;
	}

	@Override
	public List<Enrollment> findByUserId(Long userId) {
		
		return enrollRepo.findByUserId(userId);
	}

	@Override
	public Enrollment save(Enrollment enrollment) {
		return enrollRepo.save(enrollment);
	}

	@Override
	public Enrollment findByUsernameAndCourseId(String username, Integer courseId) {
		return enrollRepo.findByUsernameAndCourseId(username, courseId);
	}
	
	
	
}
