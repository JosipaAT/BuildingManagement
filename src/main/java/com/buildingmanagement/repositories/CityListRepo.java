package com.buildingmanagement.repositories;
import com.buildingmanagement.entities.CityList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.ReportAsSingleViolation;
import java.util.List;

@Repository
public interface CityListRepo extends JpaRepository <CityList, Integer>{

    @Query("SELECT c FROM CityList c WHERE c.cityName LIKE %:keyword%")
    List<CityList> searchByKeyword(@Param("keyword") String keyword);
}
