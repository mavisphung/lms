package com.lmsapp.project.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.services.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	private final CourseService courseService;
	
	@Autowired
	public CourseController(CourseService courseService) {
		this.courseService = courseService;
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
}
