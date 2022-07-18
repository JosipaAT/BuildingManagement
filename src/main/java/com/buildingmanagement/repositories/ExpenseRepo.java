package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

    @Query("SELECT e FROM Expense e WHERE e.unit.unitId = :unitId")
    Page<Expense> getAllExpenseOfUnit(@Param("unitId") Integer unitId, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.dateOfReceipt BETWEEN :startDate AND :endDate AND e.unit.unitId = :unitId")
    Page<Expense> getExpenseOfPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("unitId") Integer unitId, Pageable pageable);


    @Query("SELECT e FROM Expense e WHERE e.unit.unitId = :unitId")
    List<Expense> getAllExpenseOfUnit(@Param("unitId") Integer unitId);

    @Query("SELECT e FROM Expense e WHERE e.dateOfReceipt BETWEEN :startDate AND :endDate AND e.unit.unitId = :unitId")
    List<Expense> getExpenseOfPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("unitId") Integer unitId);
}
