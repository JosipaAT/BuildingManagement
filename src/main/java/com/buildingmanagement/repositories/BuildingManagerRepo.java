package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.BuildingManager;
import com.buildingmanagement.entities.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuildingManagerRepo extends JpaRepository<BuildingManager, Integer> {

    BuildingManager findByUsername(String username);

    Page<BuildingManager> findAll(Pageable pageable);

    @Query("SELECT b FROM BuildingManager b WHERE b.buildManagerName LIKE %:keyword%")
    Page<BuildingManager> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
