package com.lmsapp.project.activities;


import java.time.LocalDateTime;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;


import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="users_activities")
public class UserActivities {

	
	
	@Id
	@Column(name="activity_id")
	int id;
	
	
	@Column(name="username")
	String username;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name="start_date")
	LocalDateTime start;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name="end_date")
	LocalDateTime end;
	
	
	@Column(name="description")
	String text;
	

	public UserActivities() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	
	
	
	
	
}
