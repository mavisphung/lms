package com.lmsapp.project.course;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Enrollment;
import com.lmsapp.project.entities.EnrollmentKey;
import com.lmsapp.project.exception.ResponseException;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.EnrollmentService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	private final CourseService courseService;
	private final EnrollmentService enrollService;
	private final UserService userService;
	
	@Autowired
	public CourseController(CourseService courseService,
			EnrollmentService enrollService,
			UserService userService) {
		this.courseService = courseService;
		this.enrollService = enrollService;
		this.userService = userService;
	}
	
	@GetMapping(value = { "", "/" })
	public String showCoursePage(Model model) {
		List<Course> courses = courseService.findAll();
		
		model.addAttribute("courses", courses);
		return "course/index";
	}
	
	@GetMapping("/detail")
	public String showCourseDetail(
			@RequestParam("course_id") Integer courseId,
			Model model) {
		Course courseFromDb = null;
		try {
			courseFromDb = courseService.findById(courseId);
		} catch (RuntimeException e) {
			model.addAttribute("error", "Not found that course id " + courseId);
			return "error";
		}
		if (courseFromDb == null) {
			model.addAttribute("error", "Not found that course id " + courseId);
			return "error";
		}
		
		model.addAttribute("course", courseFromDb);
		model.addAttribute("modules", courseFromDb.getModules());
		return "course/detail";
	}
	
	@GetMapping("/enroll")
	public String enrollCourse(
			@RequestParam("course_id") Integer courseId,
			Principal principal, Model model) {
		String url = "redirect:";
		Enrollment enrollment = enrollService.findByUsernameAndCourseId(principal.getName(), courseId);
		if (enrollment != null) {
			String message = "You have already enrolled this course";
			model.addAttribute("message", message);
			return "enrolled-course";
		}
		
		Course courseFromDb = courseService.findById(courseId);
		User user = userService.findByUsername(principal.getName());
		
		enrollment = new Enrollment(user, courseFromDb, new Date());
		EnrollmentKey key = new EnrollmentKey(user.getId(), courseFromDb.getId());
		enrollment.setKey(key);
		Enrollment saved = enrollService.save(enrollment);
		return url + "/";
	}
	
}
