package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer unitId;
    private Integer areaSqrMeter;
    private Integer noOfTenants;

    @OneToMany(mappedBy = "unit", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Income> incomes = new ArrayList<>();

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();

    @ManyToOne
    private Floor floor;

    @ManyToOne
    private UnitType unitType;

    @ManyToOne
    private CoOwner coOwner;

    @ManyToOne
    private BuildingComplex buildingComplex;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getAreaSqrMeter() {
        return areaSqrMeter;
    }

    public void setAreaSqrMeter(Integer areaSqrMeter) {
        this.areaSqrMeter = areaSqrMeter;
    }

    public Integer getNoOfTenants() {
        return noOfTenants;
    }

    public void setNoOfTenants(Integer noOfTenants) {
        this.noOfTenants = noOfTenants;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public CoOwner getCoOwner() {
        return coOwner;
    }

    public void setCoOwner(CoOwner coOwner) {
        this.coOwner = coOwner;
    }

    public BuildingComplex getBuildingComplex() {
        return buildingComplex;
    }

    public void setBuildingComplex(BuildingComplex buildingComplex) {
        this.buildingComplex = buildingComplex;
    }
}
