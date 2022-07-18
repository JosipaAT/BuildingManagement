package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.BuildingComplex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildComplexRepo extends JpaRepository<BuildingComplex, Integer> {

    BuildingComplex findByUsername(String username);

    @Query("SELECT b FROM BuildingComplex b WHERE b.buildManager.buildManagerId = :buildManagerId")
    Page<BuildingComplex> getAllBuildComplexOfManager(@Param("buildManagerId") Integer buildManagerId, Pageable pageable);

    @Query("SELECT b FROM BuildingComplex b WHERE b.buildManager.buildManagerId = :buildManagerId AND b.username LIKE %:keyword%")
    Page<BuildingComplex> searchByKeyword(@Param("buildManagerId") Integer buildManagerId, String keyword, Pageable pageable);

}
