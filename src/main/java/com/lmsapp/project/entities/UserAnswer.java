package com.lmsapp.project.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answeredquestion")
public class UserAnswer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_quizz_id")
	private UserQuizz userQuiz;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "question_id")
	private Question question;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "answer_id")
	private Answer answer;

	public UserAnswer(UserQuizz userQuiz, Question question, Answer answer) {
		this.userQuiz = userQuiz;
		this.question = question;
		this.answer = answer;
	}
	
	
}
