package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.BuildingComplex;
import com.buildingmanagement.entities.Floor;
import com.buildingmanagement.entities.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepo extends JpaRepository<Unit, Integer> {

    @Query("SELECT u FROM Unit u WHERE u.floor.floorId = :floorId")
    Page<Unit> getAllUnitOfFloor(@Param("floorId") Integer floorId, Pageable pageable);

    @Query("SELECT u FROM Unit u WHERE u.coOwner.coOwnerId = :coOwnerId")
    List<Unit> getAllUnitOfCoOwner(@Param("coOwnerId") Integer coOwnerId);

    @Query("SELECT u FROM Unit u WHERE u.floor.floorId = :floorId AND u.coOwner.coOwnerName LIKE %:keyword%")
    Page<Unit> searchUnitByKeyword(@Param("keyword") String keyword, @Param("floorId") Integer floorId, Pageable pageable);

    @Query("SELECT u FROM Unit u WHERE u.unitType.unitTypeId = :unitTypeId AND u.floor.floorId = :floorId")
    Page<Unit> searchUnitByUnitType(@Param("unitTypeId") Integer unitTypeId, @Param("floorId") Integer floorId, Pageable pageable);
}
