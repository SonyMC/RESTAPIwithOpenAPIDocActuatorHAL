package com.in28minutes.rest.webservices.restfulwebservices.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.rest.webservices.restfulwebservices.helloworld.HelloWorldBean;

// Controller which handles http requests


@RestController
public class HelloWorldController {
	
	//GET 
	// uri -> /hello-world
	// method -> returns "Hello World!!!'

	
	
	//@RequestMapping(method=RequestMethod.GET, path="/hello-world")
	
	@GetMapping(path = "/hello-world")
	public String helloWorld(){
		return "Hello World";
	}
	
	
	//hello-world-bean
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean(){
		return new HelloWorldBean("Hello World Bean!!!");
	}
	
	//hello-world-bean/path-variable/in28minutes 
	@GetMapping(path="/hello-world-bean/path-variable/{pathname}")
	public HelloWorldBean helloWorldBeanPathVariable(@PathVariable String  pathname){
		return new HelloWorldBean(String.format("Hello World Bean!!!, %s", pathname));
	}
	
}
