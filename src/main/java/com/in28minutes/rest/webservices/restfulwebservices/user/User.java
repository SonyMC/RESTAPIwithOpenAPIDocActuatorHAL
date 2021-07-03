package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.util.Date;

import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;



public class User {
	
	@Null
	private Integer id;
	
	
	@Size(min=2, message="Name should have alteast 2 characters")
	private String name;
	
	
	@PastOrPresent
	private Date birthDate;
	
	public User() {

	}

	public User(int id, String name, Date birthDate) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
	}

	public Integer getId() {
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthDate=" + birthDate + "]";
	}
	
	
	

}
