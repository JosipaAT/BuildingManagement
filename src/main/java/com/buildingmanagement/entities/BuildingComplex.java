package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class BuildingComplex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildComplexId;
    private String streetName;
    private Integer streetNumber;
    private String city;
    private Integer postalCode;

    private String username;
    private String password;
    private String bulletin;

    @ManyToOne
    private BuildingManager buildManager;

    @OneToMany(mappedBy = "buildingComplex", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Floor> floors = new ArrayList<>();

    @OneToMany(mappedBy = "buildingComplex", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Unit> units = new ArrayList<>();

    @OneToMany(mappedBy = "buildingComplex", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Equipment> equipments = new ArrayList<>();

    @OneToMany(mappedBy = "buildingComplex", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Income> incomes = new ArrayList<>();

    @OneToMany(mappedBy = "buildingComplex", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "buildingComplex", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Info> infos = new ArrayList<>();

    @ManyToOne
    private Role role;

    public Integer getBuildComplexId() {
        return buildComplexId;
    }

    public void setBuildComplexId(Integer buildComplexId) {
        this.buildComplexId = buildComplexId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BuildingManager getBuildManager() {
        return buildManager;
    }

    public void setBuildManager(BuildingManager buildManager) {
        this.buildManager = buildManager;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }
}
