package com.in28minutes.rest.webservices.restfulwebservices.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.exception.UserNotCreatedException;
import com.in28minutes.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserDaoService;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserDaoService.Status;

@RestController
public class UserResourceController {
	
	
	@Autowired
	private UserDaoService userDaoService;
	
	//GET /users
	// retrieveAllUsers 
	
	@GetMapping(path="/users")
	@ResponseStatus(HttpStatus.FOUND)
	public List<User> retrieveAllUsers(){
		
		List<User> users = userDaoService.findAll();
		if (users.isEmpty()){
			throw new UserNotFoundException("No users found!!!");
		}
		
		
		return users;
		
	}
	
		
	// retrieveUser(int id)
	@GetMapping(path="/users/user/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public User retrieveUser(@PathVariable int id){
		
		
		
	//	User user = userDaoService.findOne(id);
		User modelUser = userDaoService.findOne(id); // because we extended USer class
		
		if(modelUser == null){
			throw new UserNotFoundException("User ID: " + id + " not found!!!");
		}
		
				

		return modelUser;
	}

		
		
	// deleteUser(int id)
	@GetMapping(path="/users/deleteuser/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> deleteUser(@PathVariable int id){
		
		Status status = userDaoService.deleteUser(id);
		
		if(status == Status.FAILURE){
			throw new UserNotFoundException("User ID: " + id + "  does not exist to be deleted!!!");
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	// create a user using POST
	// input -> details of the user
	// output -> CREATED & Return teh create uri
	
		@PostMapping("/users")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
		
			
		// We do not want Request to provide id 
		if(user.getId()!= null){
			throw new UserNotCreatedException("User could not be Created. Please check Request Format!!!");
		}
			
			

		// Save the user 
		User savedUser = userDaoService.saveUser(user);
		
		
		if(savedUser == null){
			throw new UserNotCreatedException("User could not be Created. Please check Request Format!!!");
		}
		
		
	
		
	//	return Status and uri .Can be done using REsponse entity 
	// location uri needs to be created first ; take current uri and append new user id 
	// /users/{id} ->  replace id with user.getId	
		URI location = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/user/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri();
		
		return ResponseEntity.created(location).build();
				
		
		
	}
	
	
		
	
}
