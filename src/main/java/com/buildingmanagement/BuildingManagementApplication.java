package com.buildingmanagement;

import com.buildingmanagement.entities.CoOwner;
import com.buildingmanagement.entities.Role;
import com.buildingmanagement.entities.User;
import com.buildingmanagement.entities.UserRegistration;
import com.buildingmanagement.repositories.CoOwnerRepo;
import com.buildingmanagement.repositories.UserRepository;
import com.buildingmanagement.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class BuildingManagementApplication  implements CommandLineRunner {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CoOwnerRepo coOwnerRepo;

	public static void main(String[] args) {
		SpringApplication.run(BuildingManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//UserRegistration userRegistration = new UserRegistration();
        //userRegistration.setEmail("admin@gmail.com");
	    //userRegistration.setPassword("adminuser");
	    //this.userServiceImpl.save(userRegistration);

	}
}
