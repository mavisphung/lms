package com.lmsapp.project.activities;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActivitiesController {
	@GetMapping("/calendar")
	public String cart() {
		return "activities";
	}
}
