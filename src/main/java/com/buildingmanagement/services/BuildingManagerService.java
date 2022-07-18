package com.buildingmanagement.services;

import com.buildingmanagement.entities.BuildingManager;
import org.springframework.stereotype.Service;

public interface BuildingManagerService {
    BuildingManager addBuildManager(BuildingManager buildingManager);
    BuildingManager getUserByUsername(String username);
    void update(Integer buildManagerId, BuildingManager buildingManager);
}
