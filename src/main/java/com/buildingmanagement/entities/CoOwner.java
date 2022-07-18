package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class CoOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coOwnerId;
    private String coOwnerName;
    private String contact;

    private String username;
    private String password;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "coOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Unit> units = new ArrayList<>();

    public Integer getCoOwnerId() {
        return coOwnerId;
    }

    public void setCoOwnerId(Integer coOwnerId) {
        this.coOwnerId = coOwnerId;
    }

    public String getCoOwnerName() {
        return coOwnerName;
    }

    public void setCoOwnerName(String coOwnerName) {
        this.coOwnerName = coOwnerName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}