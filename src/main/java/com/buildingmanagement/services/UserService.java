package com.buildingmanagement.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.buildingmanagement.entities.User;
import com.buildingmanagement.entities.UserRegistration;

public interface UserService extends UserDetailsService{

	User save(UserRegistration userRegistration);
	
}
