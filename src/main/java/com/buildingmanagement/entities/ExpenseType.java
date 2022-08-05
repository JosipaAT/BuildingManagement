package com.buildingmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class ExpenseType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expenseTypeId;
    private String expenseTypeName;

    @OneToMany(mappedBy = "expenseType", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Expense> expenses = new HashSet<>();

    public Integer getExpenseTypeId() {
        return expenseTypeId;
    }

    public void setExpenseTypeId(Integer expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
    }

    public String getExpenseTypeName() {
        return expenseTypeName;
    }

    public void setExpenseTypeName(String expenseTypeName) {
        this.expenseTypeName = expenseTypeName;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<UtilitiesPrice> getUtilitiesPrices() {
        return utilitiesPrices;
    }

    public void setUtilitiesPrices(Set<UtilitiesPrice> utilitiesPrices) {
        this.utilitiesPrices = utilitiesPrices;
    }

    @OneToMany(mappedBy = "expenseType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UtilitiesPrice> utilitiesPrices = new HashSet<>();


}
