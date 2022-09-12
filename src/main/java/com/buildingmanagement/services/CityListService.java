package com.buildingmanagement.services;
import com.buildingmanagement.entities.CityList;
import org.springframework.stereotype.Service;

@Service
public interface CityListService {
    CityList addNewCity(CityList cityList);
    CityList getByName(String cityName);
    void update(Integer cityId, CityList cityList);

}
