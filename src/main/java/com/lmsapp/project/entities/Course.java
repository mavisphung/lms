package com.lmsapp.project.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor

public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "username")
	private String username;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = {CascadeType.ALL})
	private List<Module> modules;

	public Course(String name, boolean isActive, String description) {
		this.name = name;
		this.isActive = isActive;
		this.description = description;
	}
}
