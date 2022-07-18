package com.buildingmanagement.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
public class Expense{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expenseId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfReceipt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    private String description;
    private Integer amount;

    @ManyToOne
    private ExpenseType expenseType;

    @ManyToOne
    private Unit unit;

    @ManyToOne
    private BuildingComplex buildingComplex;

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Date getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(Date dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public BuildingComplex getBuildingComplex() {
        return buildingComplex;
    }

    public void setBuildingComplex(BuildingComplex buildingComplex) {
        this.buildingComplex = buildingComplex;
    }
}
