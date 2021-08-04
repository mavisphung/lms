package com.lmsapp.project.activities;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    Iterable<UserActivities> events(@RequestParam("start") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end,Principal principal) {
        
		String currentUser=principal.getName();
		
		Iterable<UserActivities> list=userActivitiesRepository.findBetween(start, end);
//        for(UserActivities temp:list) {
//        	String userDescription=temp.getText();
//        	temp.setText("username:"+ temp.getUsername()+ "|activity: " + userDescription );
//        }
		return list;
    }
	
	@GetMapping("/api/events2")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    Iterable<UserActivities> events2(@RequestParam("start") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end,Principal principal) {
        
		String currentUser=principal.getName();
		
		Iterable<UserActivities> list=userActivitiesRepository.findBetweenByUsername(start, end, currentUser);
//        for(UserActivities temp:list) {
//        	String userDescription=temp.getText();
//        	temp.setText("username:"+ temp.getUsername()+ "|activity: " + userDescription );
//        }
		return list;
    }
	
	@RequestMapping("/getall")
	List<UserActivities> findAll(){
		return (List<UserActivities>) userActivitiesRepository.findAll();
	}
	

    @PostMapping("/api/events/delete")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Transactional
    EventDeleteResponse deleteEvent(@RequestBody EventDeleteParams params) {

    	userActivitiesRepository.deleteById(params.id);

        return new EventDeleteResponse() {{
            message = "Deleted";
        }};
    }

    public static class EventDeleteParams {
        public Integer id;
    }
    
    public static class EventDeleteResponse {
        public String message;
    }
	
}
