package com.buildingmanagement.entities;

import javax.persistence.*;

@Table
@Entity
public class UtilitiesPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer utilitiesPriceId;

    private Float price;
    private String unitOfMeasurement;


    @ManyToOne
    @JoinColumn(name = "expenseTypeId")
    private ExpenseType expenseType;

    @ManyToOne
    @JoinColumn(name = "unitTypeId")
    private UnitType unitType;

    @ManyToOne
    @JoinColumn(name = "buildComplexId")
    private BuildingComplex buildingComplex;

    public Integer getUtilitiesPriceId() {
        return utilitiesPriceId;
    }

    public void setUtilitiesPriceId(Integer utilitiesPriceId) {
        this.utilitiesPriceId = utilitiesPriceId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public BuildingComplex getBuildingComplex() {
        return buildingComplex;
    }

    public void setBuildingComplex(BuildingComplex buildingComplex) {
        this.buildingComplex = buildingComplex;
    }
}
