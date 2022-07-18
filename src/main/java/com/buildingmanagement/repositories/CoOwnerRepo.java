package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.BuildingComplex;
import com.buildingmanagement.entities.CoOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoOwnerRepo extends JpaRepository<CoOwner, Integer> {
    CoOwner findByUsername(String username);
}
