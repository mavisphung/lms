package com.lmsapp.project.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lmsapp.project.exception.ResponseException;

@ControllerAdvice
public class CourseApiAdvice {
	
	@ExceptionHandler(ResponseException.class)
	public ResponseEntity<ResponseException> handleResponseException() {
		
		ResponseException exception = new ResponseException();
		exception.setCode(HttpStatus.METHOD_NOT_ALLOWED.toString());
		exception.setMessage("You have already enrolled this course.");
		return new ResponseEntity<ResponseException>(exception, HttpStatus.METHOD_NOT_ALLOWED);
		
	}
}
