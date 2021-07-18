package com.lmsapp.project.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "authority_id") })
    private Set<Authority> authorities = new HashSet<>();
	
	public User(String username, String password, String email, String address, boolean enabled, String firstName,
			String lastName, Set<Authority> authorities) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.address = address;
		this.enabled = enabled;
		this.firstName = firstName;
		this.lastName = lastName;
		this.authorities = authorities;
	}
	
}