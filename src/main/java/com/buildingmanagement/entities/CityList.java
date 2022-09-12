package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class CityList {
    public CityList() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cityId;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    @OneToMany(mappedBy = "cityList", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BuildingComplex> buildingComplexes = new ArrayList<>();

    @OneToMany(mappedBy = "cityList",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Info> infos = new ArrayList<>();

    @OneToMany(mappedBy = "cityList",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private  List<BuildingManager> buildingManagers = new ArrayList<>();
}
