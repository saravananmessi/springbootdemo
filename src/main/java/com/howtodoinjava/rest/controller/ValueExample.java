package com.howtodoinjava.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValueExample {
	
	
	@Value("${app.title}")
	@Autowired
	private String apptitle;

	 @GetMapping("/value")
	 public String getValue()
	 {
	 return apptitle;
	 }
	 

}
