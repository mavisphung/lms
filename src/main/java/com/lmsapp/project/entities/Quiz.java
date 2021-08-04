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

@Entity
@Table(name = "quizzes")
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "time_allow")
	private int timeAllow;
	
	@ManyToOne(cascade = { CascadeType.PERSIST,
							CascadeType.MERGE,
							CascadeType.DETACH,
							CascadeType.REFRESH})
	@JoinColumn(name = "module_id")
	private Module module;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz", cascade = {CascadeType.ALL})
	private List<Question> questions;
	
	@OneToMany(mappedBy = "quiz", cascade = {CascadeType.ALL})
	private List<UserQuizz> userQuizzs;
	
	public Quiz() {
	}

	
	public Quiz(String name, int timeAllow) {
		this.name = name;
		this.timeAllow = timeAllow;
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

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public int getTimeAllow() {
		return timeAllow;
	}

	public void setTimeAllow(int timeAllow) {
		this.timeAllow = timeAllow;
	}

	@Override
	public String toString() {
		return "Quiz [name=" + name + ", timeAllow=" + timeAllow + "]";
	}
}
