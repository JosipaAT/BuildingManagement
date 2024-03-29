package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.Expense;
import com.buildingmanagement.entities.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ExpenseTypeRepo extends JpaRepository<ExpenseType, Integer> {
    List<ExpenseType> findByExpenseTypeNameIn(Collection<String> names);
    List<ExpenseType> findByExpenseTypeNameNotIn(Collection<String> names);

}
