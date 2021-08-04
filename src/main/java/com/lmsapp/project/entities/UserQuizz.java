package com.lmsapp.project.entities;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.lmsapp.project.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_quizzes")
public class UserQuizz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quizz_id")
	private Quiz quiz;

	@Column(name = "score")
	private float score;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userQuiz", cascade = CascadeType.ALL)
	private List<UserAnswer> userAnswers;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "doing_date")
	private Date doingDate;

	public UserQuizz(float score) {
		this.score = score;
	}
}
