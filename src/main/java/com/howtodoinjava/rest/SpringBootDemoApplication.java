package com.howtodoinjava.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication 
public class SpringBootDemoApplication extends SpringBootServletInitializer{
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
        LOGGER.debug("Simple log statement with inputs {}, {} and {}", 1,2,3);
    }
}