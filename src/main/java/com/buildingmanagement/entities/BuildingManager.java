package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class BuildingManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildManagerId;
    private String buildManagerName;
    private String streetName;
    private Integer streetNumber;
    private String city;
    private Integer postalCode;
    private Integer contact;

    private String username;
    private String password;

    @OneToMany(mappedBy = "buildManager", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BuildingComplex> buildingComplexes = new ArrayList<>();

    @ManyToOne
    private Role role;

    public Integer getBuildManagerId() {
        return buildManagerId;
    }

    public void setBuildManagerId(Integer buildManagerId) {
        this.buildManagerId = buildManagerId;
    }

    public String getBuildManagerName() {
        return buildManagerName;
    }

    public void setBuildManagerName(String buildManagerName) {
        this.buildManagerName = buildManagerName;
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

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
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

    public List<BuildingComplex> getBuildingComplexes() {
        return buildingComplexes;
    }

    public void setBuildingComplexes(List<BuildingComplex> buildingComplexes) {
        this.buildingComplexes = buildingComplexes;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
