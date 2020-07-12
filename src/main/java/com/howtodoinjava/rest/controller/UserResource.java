package com.howtodoinjava.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howtodoinjava.rest.dao.UserDaoService;
import com.howtodoinjava.rest.user.User;

@RestController
public class UserResource {
	
	@Autowired
	private UserDaoService service;
	
	@RequestMapping(value="/")
    public String test() 
    {
        return "Heloooo";
    }
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers()
	{
		return service.findAll();
	}
	
	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable int id)
	{
		return service.findOne(id);
	}

}
