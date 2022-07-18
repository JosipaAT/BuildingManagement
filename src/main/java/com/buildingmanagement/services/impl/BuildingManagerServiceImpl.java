package com.buildingmanagement.services.impl;

import com.buildingmanagement.entities.BuildingManager;
import com.buildingmanagement.entities.Role;
import com.buildingmanagement.entities.User;
import com.buildingmanagement.repositories.BuildingManagerRepo;
import com.buildingmanagement.repositories.UserRepository;
import com.buildingmanagement.services.BuildingManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BuildingManagerServiceImpl implements BuildingManagerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuildingManagerRepo buildingManagerRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public BuildingManager addBuildManager(BuildingManager buildingManager) {
        Role role = new Role();
        role.setRoleId(2);
        buildingManager.setRole(role);
        User user = new User(buildingManager.getUsername(), passwordEncoder.encode(buildingManager.getPassword()) , Arrays.asList(role));
        userRepository.save(user);
        return buildingManagerRepo.save(buildingManager);
    }

    @Override
    public BuildingManager getUserByUsername(String username) throws UsernameNotFoundException {

        BuildingManager buildingManager = buildingManagerRepo.findByUsername(username);

        if (buildingManager == null) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }

        return buildingManager;
    }

    @Override
    public void update(Integer buildManagerId, BuildingManager buildingManager) {
        BuildingManager buildingManager1 = this.buildingManagerRepo.findById(buildManagerId).get();
        buildingManager1.setBuildManagerName(buildingManager.getBuildManagerName());
        buildingManager1.setStreetName(buildingManager.getStreetName());
        buildingManager1.setStreetNumber(buildingManager.getStreetNumber());
        buildingManager1.setCity(buildingManager.getCity());
        buildingManager1.setPostalCode(buildingManager.getPostalCode());
        buildingManager1.setContact(buildingManager.getContact());
        this.buildingManagerRepo.save(buildingManager1);

    }


}
