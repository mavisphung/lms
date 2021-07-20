package com.lmsapp.project.activities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;



@RestController
public class ApiActivitiesController {

	@Autowired
	UserActivitiesRepository userActivitiesRepository;
	
	@GetMapping("/api/events")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    Iterable<UserActivities> events(@RequestParam("start") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start, @RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        return userActivitiesRepository.findBetween(start, end);
    }
	
	@RequestMapping("/getall")
	List<UserActivities> findAll(){
		return (List<UserActivities>) userActivitiesRepository.findAll();
	}
	
//	@RequestMapping("/getDate")
//	List<UserActivities> getByDate(){
//		return (List<UserActivities>) userActivitiesRepository;
//	}
	
}
