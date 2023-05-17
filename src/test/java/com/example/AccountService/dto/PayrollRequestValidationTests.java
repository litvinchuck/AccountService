package com.example.AccountService.dto;

import com.example.AccountService.dto.payroll.PayrollRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static com.example.AccountService.test_utils.TestConstants.UserDetails.EMAIL;
import static com.example.AccountService.test_utils.ValidationUtils.*;

public class PayrollRequestValidationTests {

    private PayrollRequest correctPayrollRequest;

    @BeforeEach
    void setUpBeforeEach() {
        correctPayrollRequest = PayrollRequest.builder()
                .salary(100L)
                .period(YearMonth.now())
                .employeeEmail(EMAIL)
                .build();
    }

    @Test
    @DisplayName("Correct PayrollRequest passes validation")
    void correctPayroll() {
        assertNoValidationViolations(correctPayrollRequest);
    }

    @Test
    @DisplayName("Payroll request with null email has one validation violation")
    void nullEmail() {
        correctPayrollRequest.setEmployeeEmail(null);
        assertOneValidationViolation(correctPayrollRequest);
    }

    @Test
    @DisplayName("Payroll request with empty email has two validation violations")
    void emptyEmail() {
        correctPayrollRequest.setEmployeeEmail("");
        assertNValidationViolations(2, correctPayrollRequest);
    }

    @Test
    @DisplayName("Payroll request with correct email but wrong domain has one validation violation")
    void correctEmailWrongDomain() {
        correctPayrollRequest.setEmployeeEmail("jdoe@gmail.com");
        assertOneValidationViolation(correctPayrollRequest);
    }

    @Test
    @DisplayName("Payroll request with wrong email but correct domain has one validation violation")
    void wrongEmailCorrectDomain() {
        correctPayrollRequest.setEmployeeEmail("A@b@c@acme.com");
        assertOneValidationViolation(correctPayrollRequest);
    }

    @Test
    @DisplayName("Payroll request with zero salary has no validation violations")
    void zeroSalary() {
        correctPayrollRequest.setSalary(0L);
        assertNoValidationViolations(correctPayrollRequest);
    }

    @Test
    @DisplayName("Payroll request with negative salary has one validation violation")
    void negativeSalary() {
        correctPayrollRequest.setSalary(-100L);
        assertOneValidationViolation(correctPayrollRequest);
    }

}
