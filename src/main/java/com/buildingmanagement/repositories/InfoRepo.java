package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfoRepo extends JpaRepository<Info, Integer> {

    @Query("SELECT i FROM Info i WHERE i.buildingComplex.buildComplexId = :buildComplexId")
    List<Info> getAllInfoOfBuild(@Param("buildComplexId") Integer buildComplexId);

    @Query("SELECT i FROM Info i WHERE i.serviceName LIKE %:keyword% AND i.buildingComplex.buildComplexId = :buildComplexId")
    List<Info> searchInfoByKeyword(@Param("buildComplexId") Integer buildComplexId, @Param("keyword") String keyword);
}
