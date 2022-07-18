package com.buildingmanagement.services.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.buildingmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.buildingmanagement.entities.Role;
import com.buildingmanagement.entities.User;
import com.buildingmanagement.entities.UserRegistration;
import com.buildingmanagement.repositories.UserRepository;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public User save(UserRegistration userRegistration) {
		Role role = new Role();
		role.setRoleId(1);
		User user = new User(userRegistration.getEmail(), passwordEncoder.encode(userRegistration.getPassword()) , Arrays.asList(role));
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password![1]");
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
	}

}
