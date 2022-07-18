package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer floorId;
    private String floorName;

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Unit> units = new ArrayList<>();

    @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Equipment> equipments = new ArrayList<>();

    @ManyToOne
    private BuildingComplex buildingComplex;

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public BuildingComplex getBuildingComplex() {
        return buildingComplex;
    }

    public void setBuildingComplex(BuildingComplex buildingComplex) {
        this.buildingComplex = buildingComplex;
    }
}
