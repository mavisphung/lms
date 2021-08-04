package com.lmsapp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsapp.project.entities.Enrollment;
import com.lmsapp.project.entities.EnrollmentKey;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentKey> {
	
	
	@Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId")
	List<Enrollment> findByUserId(@Param("userId") Long userId);
	
	@Query("SELECT e FROM Enrollment e WHERE e.user.username = :username AND e.course.id = :course_id")
	Enrollment findByUsernameAndCourseId(@Param("username") String username, @Param("course_id") Integer courseId);
}
