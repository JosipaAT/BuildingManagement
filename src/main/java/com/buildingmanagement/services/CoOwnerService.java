package com.buildingmanagement.services;

import com.buildingmanagement.entities.BuildingComplex;
import com.buildingmanagement.entities.CoOwner;

public interface CoOwnerService {
    void addCoOwner(CoOwner coOwner, Integer coOwnerId, Integer unitId);
    CoOwner getCoOwnerByUsername(String username);
}
