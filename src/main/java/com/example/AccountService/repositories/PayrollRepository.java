package com.example.AccountService.repositories;

import com.example.AccountService.models.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Payroll getPayrollByUserEmail(String email);
    boolean existsByUserEmail(String email);
    Long countByUserEmail(String email);
}
