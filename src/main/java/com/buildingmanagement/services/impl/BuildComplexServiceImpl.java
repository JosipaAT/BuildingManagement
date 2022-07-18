package com.buildingmanagement.services.impl;

import com.buildingmanagement.entities.BuildingComplex;
import com.buildingmanagement.entities.BuildingManager;
import com.buildingmanagement.entities.Role;
import com.buildingmanagement.entities.User;
import com.buildingmanagement.repositories.BuildComplexRepo;
import com.buildingmanagement.repositories.BuildingManagerRepo;
import com.buildingmanagement.repositories.UserRepository;
import com.buildingmanagement.services.BuildComplexService;
import com.buildingmanagement.services.BuildingManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BuildComplexServiceImpl implements BuildComplexService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuildComplexRepo buildingComplexRepo;

    @Autowired
    private BuildingManagerService buildingManagerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public BuildingComplex addBuildComplex(BuildingComplex buildingComplex) {
        Role role = new Role();
        role.setRoleId(3);
        buildingComplex.setRole(role);
        User user = new User(buildingComplex.getUsername(), passwordEncoder.encode(buildingComplex.getPassword()) , Arrays.asList(role));
        userRepository.save(user);
        return buildingComplexRepo.save(buildingComplex);
    }

    @Override
    public BuildingComplex getBuildingByUsername(String username) throws UsernameNotFoundException {

        BuildingComplex buildingComplex = buildingComplexRepo.findByUsername(username);

        if (buildingComplex == null) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }

        return buildingComplex;
    }
}
