package com.howtodoinjava.rest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.howtodoinjava.rest.user.User;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList();
	
	int usersCount = 3;
	
	static
	{
		users.add(new User(1, "saravanan",new Date()));
		users.add(new User(2, "sss",new Date()));
		users.add(new User(3, "ramesh",new Date()));
		
	}
	
	public List<User> findAll()
	{
		return users;
		
	}
	
	public User save(User user)
	{
		
		if(user.getId() == null)
		{
			
			user.setId(++usersCount);
		}
		
		users.add(user);
		return user;
		
	}
	
	public User findOne(int id)
	{
		for(User user : users)
		{
			if(user.getId() == id)
			{
				return user;
			}
		}
		
		return null;
	}

}
