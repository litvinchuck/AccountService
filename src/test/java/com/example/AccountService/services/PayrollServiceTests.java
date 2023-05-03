package com.example.AccountService.services;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.config.TestConfig;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.example.AccountService.dto.user_request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.time.YearMonth;
import java.util.List;

import static com.example.AccountService.test_utils.TestConstants.UserDetails.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PayrollServiceTests {

    //TODO: test employee-period pair uniquiness
    //TODO: test employee is a current user
    //TODO:

    @Autowired
    private PayrollService payrollService;

    private PayrollRequest correctPayrollRequest;

    private List<PayrollRequest> correctRequestsList;

    private UserRequest correctUserRequest = UserRequest.builder()
            .name(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .build();

    @BeforeEach
    void setUp() {
        correctPayrollRequest = PayrollRequest.builder()
                .salary(100)
                .period(YearMonth.now())
                .employeeEmail(EMAIL)
                .build();
        correctRequestsList = List.of(correctPayrollRequest);
    }

    @Test
    @DisplayName("Correct payrolls pass")
    void correctPayrolls() {

    }

    @Test
    @DisplayName("Payroll for a non-existing user fails")
    void notAUser() {

    }

    @Test
    @DisplayName("Payroll list with multiple salaries for one user fails")
    void nonUniquePeriodUserPair() {

    }

}
