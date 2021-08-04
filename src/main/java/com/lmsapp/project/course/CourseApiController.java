package com.lmsapp.project.course;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmsapp.project.entities.Content;
import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Enrollment;
import com.lmsapp.project.entities.EnrollmentKey;
import com.lmsapp.project.exception.ResponseException;
import com.lmsapp.project.services.ContentService;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.EnrollmentService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;

@RestController
@RequestMapping("/course")
public class CourseApiController {
	
	private final CourseService courseService;
	private final UserService userService;
	private final EnrollmentService enrollService;
	private final ContentService contentService;
	
	@Autowired
	public CourseApiController(
			CourseService courseService, 
			UserService userService,
			EnrollmentService enrollService,
			ContentService contentService) {
		this.courseService = courseService;
		this.userService = userService;
		this.enrollService = enrollService;
		this.contentService = contentService;
	}
	
	@PostMapping("/api/enroll/{course_id}")
	public ResponseEntity<Object> searchResult(
			@PathVariable("course_id") Integer courseId,
			Principal principal) throws ResponseException {
		
		Enrollment enrollment = enrollService.findByUsernameAndCourseId(principal.getName(), courseId);
		if (enrollment != null) {
			String message = "You have already enrolled this course";
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED.toString(), message);
		}
		
		Course courseFromDb = courseService.findById(courseId);
		User user = userService.findByUsername(principal.getName());
//		Enrollment enrollment = new Enrollment(user, courseFromDb, new Date());
		
		enrollment = new Enrollment(user, courseFromDb, new Date());
		EnrollmentKey key = new EnrollmentKey(user.getId(), courseFromDb.getId());
		enrollment.setKey(key);
		Enrollment saved = enrollService.save(enrollment);
		return new ResponseEntity<Object>(saved, HttpStatus.OK);
	}
	
	@GetMapping("/api/download/{contentId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("contentId") Integer contentId) {
		Content content = contentService.findById(contentId);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(content.getExtension()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + content.getName() + "\"")
				.body(new ByteArrayResource(content.getData()));
	}
}