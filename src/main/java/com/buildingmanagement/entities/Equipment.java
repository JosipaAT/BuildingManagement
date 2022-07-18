package com.buildingmanagement.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer equipmentId;
    private String equipmentName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfService;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date nextServiceDate;

    @ManyToOne
    private BuildingComplex buildingComplex;

    @ManyToOne
    private Floor floor;

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Date getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(Date dateOfService) {
        this.dateOfService = dateOfService;
    }

    public Date getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(Date nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public BuildingComplex getBuildingComplex() {
        return buildingComplex;
    }

    public void setBuildingComplex(BuildingComplex buildingComplex) {
        this.buildingComplex = buildingComplex;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
