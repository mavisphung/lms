package com.lmsapp.project.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "contents")
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "extension")
	private String extension;

	@Lob
	private byte[] data;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "module_id")
	private Module module;
	
	public Content(String name, String extension, byte[] data) {
		this.name = name;
		this.extension = extension;
		this.data = data;
	}

}
