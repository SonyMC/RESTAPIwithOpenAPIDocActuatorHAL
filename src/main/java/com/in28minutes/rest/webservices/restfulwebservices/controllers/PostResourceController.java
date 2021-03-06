package com.in28minutes.rest.webservices.restfulwebservices.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.in28minutes.rest.webservices.restfulwebservices.exception.PostNotCreatedException;
import com.in28minutes.rest.webservices.restfulwebservices.exception.PostNotFoundException;
import com.in28minutes.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.in28minutes.rest.webservices.restfulwebservices.post.Post;
import com.in28minutes.rest.webservices.restfulwebservices.post.PostDaoService;
import com.in28minutes.rest.webservices.restfulwebservices.post.PostDaoService.Status;


@RestController
public class PostResourceController {
	
	@Autowired
	private PostDaoService postService;
	
	
	
	// Get all posts 
	@GetMapping(path="/posts")
	@ResponseStatus(HttpStatus.FOUND)
	public List<Post>  retrieveAllPosts(){
		List<Post> posts = postService.findAllPosts();
		
		if(posts.isEmpty()){
			throw new PostNotFoundException("No posts Found!!!");
		}
		
		return posts;
	}
	
	
	// Get all posts belonging to a particular user 
	@GetMapping(path="/posts/post/user/{userID}")
	@ResponseStatus(HttpStatus.FOUND)
	public List<Post>  retrieveAllUserPosts(@PathVariable int userID){
		
		List<Post> userPosts = postService.findAllPostsbyUser(userID);
		
		if(userPosts.isEmpty()){
			throw new PostNotFoundException("No posts Found for the user " + userID + " !!!");
		}
		
		return userPosts;
	}
	
	
	// get a particular post 
	@GetMapping(path="/posts/post/{postID}")
	@ResponseStatus(HttpStatus.OK)
	
	public RepresentationModel<Post> retrievePost(@PathVariable int postID){
	//public Post retrievePost(@PathVariable int postID){
		
		
		RepresentationModel<Post> modelPost = postService.findOnePost(postID);
		//Post post = postService.findOnePost(postID);
		
		
		//if(post == null){
		if(modelPost == null){
			throw new PostNotFoundException("No post found with the post ID : " + postID + " !!!");
		}
	
	Link linkto = linkTo(methodOn(this.getClass()).retrieveAllPosts()).withSelfRel();
	return modelPost.add(linkto.withRel("All - Posts"));
	

	}
	
	
	
	// delete a particular post 
	@GetMapping(path="/posts/deletepost/{postID}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> deleteUser(@PathVariable int postID){
		
		Status status = postService.deletePost(postID);
		
		if(status == Status.FAILURE){
			throw new PostNotFoundException("Post ID: " + postID + "  does not exist to be deleted!!!");
		}
		
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	// create a POST
	// input -> details of the POST
	// output -> CREATED & Return teh created uri
	
		@PostMapping("/posts")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<Object> createPost(@Valid @RequestBody Post post){
		
			
		// We do not want Request to provide the post id 
		if(post.getPostId() != null)  {
			throw new PostNotCreatedException("Do not provide post id!!!");
		}
			
			
		// Save the post
		Post savedPost = postService.savePost(post);
		
		
		if(savedPost == null){
			throw new PostNotCreatedException("Post could not be Created. Please check Request Format!!!");
		}
		
		
	//	return Status and uri .Can be done using REsponse entity 
	// location uri needs to be created first ; take current uri and append new user id 
	// /users/{id} ->  replace id with user.getId
		
		URI postLocation = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/post/{postID}")
						.buildAndExpand(savedPost.getPostId())
						.toUri();

		
		return ResponseEntity.created(postLocation).build();
				
		
		
	}

}
