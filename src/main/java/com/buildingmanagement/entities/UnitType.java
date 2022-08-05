package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class UnitType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer unitTypeId;
    private String unitTypeName;

    @OneToMany(mappedBy = "unitType", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Unit> units = new ArrayList<>();


    public Integer getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(Integer unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public Set<UtilitiesPrice> getUtilitiesPrices() {
        return utilitiesPrices;
    }

    public void setUtilitiesPrices(Set<UtilitiesPrice> utilitiesPrices) {
        this.utilitiesPrices = utilitiesPrices;
    }

    @OneToMany(mappedBy = "unitType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UtilitiesPrice> utilitiesPrices = new HashSet<>();
}
