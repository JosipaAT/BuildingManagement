package com.buildingmanagement.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.buildingmanagement.entities.User;
import com.buildingmanagement.entities.UserRegistration;

import java.util.List;

public interface UserService extends UserDetailsService{

	User save(UserRegistration userRegistration);


}
