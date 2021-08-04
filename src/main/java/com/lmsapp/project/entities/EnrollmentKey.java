package com.lmsapp.project.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentKey implements Serializable {

	@Column(name = "user_id")
	private Long user_id;
	
	@Column(name = "course_id")
	private Integer courseId;
	
}
