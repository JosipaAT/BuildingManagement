package com.buildingmanagement.repositories;

import com.buildingmanagement.entities.Expense;
import com.buildingmanagement.entities.Income;
import com.buildingmanagement.entities.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IncomeRepo extends JpaRepository<Income, Integer> {

    List<Income> findByExpenseType_expenseTypeNameInAndDateOfPaymentAfterAndDateOfPaymentBefore(Collection<String> names, Date startDate, Date endDate);


    @Query("SELECT i FROM Income i WHERE i.unit.unitId = :unitId")
    Page<Income> getAllIncomeOfUnit(@Param("unitId") Integer unitId, Pageable pageable);

    @Query("SELECT i FROM Income i WHERE i.dateOfPayment BETWEEN :startDate AND :endDate AND i.unit.unitId = :unitId")
    Page<Income> getIncomeOfPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("unitId") Integer unitId, Pageable pageable );

    @Query("SELECT i FROM Income i WHERE i.unit.unitId = :unitId")
    List<Income> getAllIncomeOfUnit(@Param("unitId") Integer unitId);

    @Query("SELECT i FROM Income i WHERE i.dateOfPayment BETWEEN :startDate AND :endDate AND i.unit.unitId = :unitId")
    List<Income> getIncomeOfPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("unitId") Integer unitId);

    List<Income> findAllByExpenseType_expenseTypeName(String expenseTypeName);

    List<Income> findAllByExpenseType_expenseTypeNameAndBuildingComplex_buildComplexId(String expenseTypeName, Integer buildComplexId);

    List<Income> findAllByExpenseType_expenseTypeNameAndBuildingComplex_buildComplexIdAndUnit_unitId(String expenseTypeName, Integer buildComplexId, Integer unitId);
}
