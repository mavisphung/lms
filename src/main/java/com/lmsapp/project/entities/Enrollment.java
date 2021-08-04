package com.lmsapp.project.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.lmsapp.project.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_courses")
@Data
@NoArgsConstructor
public class Enrollment {

	@EmbeddedId
	private EnrollmentKey key;
	
	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("course_Id")
	@JoinColumn(name = "course_id")
	private Course course;
	
	@Column(name = "enrolled_date")
	@Temporal(TemporalType.DATE)
	private Date enrolledDate;
	
	public Enrollment(User user, Course course, Date enrolledDate) {
		this.enrolledDate = enrolledDate;
		this.user = user;
		this.course = course;
	}
}
