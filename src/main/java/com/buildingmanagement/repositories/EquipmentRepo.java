package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.Equipment;
import com.buildingmanagement.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepo extends JpaRepository<Equipment, Integer> {

    @Query("SELECT e FROM Equipment e WHERE e.floor.floorId = :floorId")
    List<Equipment> getAllEquipmentOfFloor(@Param("floorId") Integer floorId);
}
