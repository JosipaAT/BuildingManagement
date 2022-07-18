package com.buildingmanagement.services.impl;

import com.buildingmanagement.entities.*;
import com.buildingmanagement.repositories.BuildComplexRepo;
import com.buildingmanagement.repositories.CoOwnerRepo;
import com.buildingmanagement.repositories.UnitRepo;
import com.buildingmanagement.repositories.UserRepository;
import com.buildingmanagement.services.CoOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CoOwnerServiceImpl implements CoOwnerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private CoOwnerRepo coOwnerRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void addCoOwner(CoOwner coOwner, Integer coOwnerId,Integer unitId) {
        if(coOwnerId==0) {
            Role role = new Role();
            role.setRoleId(4);
            coOwner.setRole(role);
            User user = new User(coOwner.getUsername(), passwordEncoder.encode(coOwner.getPassword()), Arrays.asList(role));
            userRepository.save(user);
            this.coOwnerRepo.save(coOwner);
            Unit unit = this.unitRepo.findById(unitId).get();
            unit.setCoOwner(coOwner);
            this.unitRepo.save(unit);
        }
        else{
            CoOwner coOwner1 = this.coOwnerRepo.findById(coOwnerId).get();
            Unit unit = this.unitRepo.findById(unitId).get();
            unit.setCoOwner(coOwner1);
            this.unitRepo.save(unit);
        }

    }

    @Override
    public CoOwner getCoOwnerByUsername(String username) throws UsernameNotFoundException {

        CoOwner coOwner = coOwnerRepo.findByUsername(username);

        if (coOwner == null) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }

        return coOwner;
    }

}
