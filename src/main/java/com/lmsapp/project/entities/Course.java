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

@Entity
@Table(name = "courses")
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = {CascadeType.ALL})
	private List<Module> modules;
	
	public Course() {
	}

	public Course(String name, boolean isActive, String description) {
		this.name = name;
		this.isActive = isActive;
		this.description = description;
	}
	
	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", createDate=" + createDate + ", isActive=" + isActive
				+ ", description=" + description + "]";
	}

	public void add(Module tempModule) {
		if(modules == null) {
			modules = new ArrayList<>();
		}
		modules.add(tempModule);
		tempModule.setCourse(this);
	}
}
