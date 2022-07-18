package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	private String roleName;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BuildingComplex> buildingComplexes = new ArrayList<>();

	@OneToMany(mappedBy = "role",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CoOwner> coOwners = new ArrayList<>();

	@OneToMany(mappedBy = "role",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private  List<BuildingManager> buildingManagers = new ArrayList<>();

	public Role() {
	}

	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<BuildingComplex> getBuildingComplexes() {
		return buildingComplexes;
	}

	public void setBuildingComplexes(List<BuildingComplex> buildingComplexes) {
		this.buildingComplexes = buildingComplexes;
	}

	public List<CoOwner> getCoOwners() {
		return coOwners;
	}

	public void setCoOwners(List<CoOwner> coOwners) {
		this.coOwners = coOwners;
	}

	public List<BuildingManager> getBuildingManagers() {
		return buildingManagers;
	}

	public void setBuildingManagers(List<BuildingManager> buildingManagers) {
		this.buildingManagers = buildingManagers;
	}
}
