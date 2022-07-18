package com.buildingmanagement.services;

import com.buildingmanagement.entities.BuildingComplex;

public interface BuildComplexService {
    BuildingComplex addBuildComplex(BuildingComplex buildComplex);
    BuildingComplex getBuildingByUsername(String username);

}
