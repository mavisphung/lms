package com.lmsapp.project.enties;

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
	
	@ManyToOne(cascade = { CascadeType.PERSIST,
							CascadeType.MERGE,
							CascadeType.DETACH,
							CascadeType.REFRESH})
	@JoinColumn(name = "module_id")
	private Module module;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz", cascade = {CascadeType.ALL})
	private List<Question> questions;
	
	public Quiz() {
	}

	public Quiz(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "Quiz [id=" + id + ", name=" + name + "]";
	}
}
