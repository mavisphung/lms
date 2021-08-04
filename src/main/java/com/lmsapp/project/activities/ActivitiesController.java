package com.lmsapp.project.activities;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lmsapp.project.user.repository.UserRepository;

@Controller
public class ActivitiesController {
	@Autowired
	UserActivitiesRepository userActivitiesRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/calendar")
	public String cart() {
		return "activities";
	}

	@GetMapping("/createActivities")
	public ModelAndView createActivitiesPage(Principal principal) {
		ModelAndView mv = new ModelAndView("addActivities");

		mv.addObject("listName", userRepository.findAll());

		return mv;
	}

	@PostMapping("/createActivities")
	public ModelAndView create(@RequestParam(value = "username") String username,
			@RequestParam(value = "start") String start, @RequestParam(value = "end") String end,
			@RequestParam(value = "text") String description) {
		ModelAndView mv = new ModelAndView("addActivities");
		mv.addObject("listName", userRepository.findAll());
		try {
			String str = start;
			str += " 00:00";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTimestart = LocalDateTime.parse(str, formatter);

			String strEnd = end;
			strEnd += " 00:00";
			DateTimeFormatter formatterend = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTimeend = LocalDateTime.parse(strEnd, formatterend);

			if (dateTimestart.isAfter(dateTimeend) || dateTimestart.isEqual(dateTimeend)) {
				mv.addObject("error", "date is wrong");
				return mv;
			}

			UserActivities userActivities = new UserActivities();
			userActivities.setId(userActivitiesRepository.getMaxId() + 1);
			userActivities.setUsername(username);
			userActivities.setStart(dateTimestart);
			userActivities.setEnd(dateTimeend);
			userActivities.setText(description);
			userActivitiesRepository.save(userActivities);
			mv.addObject("error", "add success");

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("error", "some thing not right");
		}

		return mv;
	}
	
	@GetMapping("/findActivities")
	public ModelAndView findActivitiesByUsername() {
		
		ModelAndView mv=new ModelAndView("findActivities");	
		mv.addObject("listName", userRepository.findAll());
		
		return mv;
	}
	@PostMapping("findActivities")
	public ModelAndView findActivitiesByUsernamePost(@RequestParam(value = "username")String name) {
		ModelAndView mv=new ModelAndView("updateActivities");	
		mv.addObject("listActivities", userActivitiesRepository.findActivitiesByname(name));
		
		if(userActivitiesRepository.findActivitiesByname(name).isEmpty()) {
			ModelAndView modelview=new ModelAndView("findActivities");	
			modelview.addObject("error", "this account dont have any activities");
			modelview.addObject("listName", userRepository.findAll());
			return modelview;
		}
		
		return mv;
	}
	
	@PostMapping("/updateActivities")
	public ModelAndView update(@RequestParam(value = "username") String username,
			@RequestParam(value = "start") String start, @RequestParam(value = "end") String end,
			@RequestParam(value = "text") String description,@RequestParam(value="id")int id) {
		ModelAndView mv = new ModelAndView("updateActivities");
		mv.addObject("listActivities", userActivitiesRepository.findActivitiesByname(username));
		try {
			String str = start;
			str += " 00:00";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTimestart = LocalDateTime.parse(str, formatter);

			String strEnd = end;
			strEnd += " 00:00";
			DateTimeFormatter formatterend = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTimeend = LocalDateTime.parse(strEnd, formatterend);

			if (dateTimestart.isAfter(dateTimeend) || dateTimestart.isEqual(dateTimeend)) {
				mv.addObject("error", "date is wrong");
				return mv;
			}

			UserActivities userActivities = new UserActivities();
			userActivities.setId(id);
			userActivities.setUsername(username);
			userActivities.setStart(dateTimestart);
			userActivities.setEnd(dateTimeend);
			userActivities.setText(description);
			userActivitiesRepository.save(userActivities);
			mv.addObject("error", "update success");
		}
		catch (Exception e) {
			e.printStackTrace();
			mv.addObject("error", "some thing not right");
			return mv;
		}
		
		mv.addObject("error", "update success");
		return mv;
		
		
	}
	
	
	
	
	
}
