package com.lmsapp.project.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modules")
@NoArgsConstructor
@Data
public class Module {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(cascade = {CascadeType.PERSIST,
							CascadeType.MERGE,
							CascadeType.DETACH,
							CascadeType.REFRESH})
	@JoinColumn(name="course_id")
	private Course course;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module", cascade = {CascadeType.ALL})
	private List<Content> contents;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module", cascade = {CascadeType.ALL})
	private List<Quiz> quizzes;
	
	public Module(String name) {
		this.name = name;
	}
	
	
}
