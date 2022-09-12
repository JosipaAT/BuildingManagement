package com.buildingmanagement.services.impl;
import com.buildingmanagement.entities.CityList;
import com.buildingmanagement.repositories.CityListRepo;
import com.buildingmanagement.services.CityListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityListServiceImpl implements CityListService {

    @Autowired
    private CityListRepo cityListRepo;

    @Override
    public CityList addNewCity(CityList cityList) {

        this.cityListRepo.save(cityList);
        return null;
    }

    @Override
    public CityList getByName(String cityName) {
        return null;
    }

    @Override
    public void update(Integer cityId, CityList cityList) {

    }
}
