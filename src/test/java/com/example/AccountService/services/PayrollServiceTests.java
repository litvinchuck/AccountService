package com.example.AccountService.services;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.config.TestConfig;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.example.AccountService.dto.user_request.UserRequest;
import com.example.AccountService.exceptions.UserDoesNotExistException;
import com.example.AccountService.exceptions.UserHasMultipleSalariesException;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PayrollServiceTests {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private PayrollRequest correctPayrollRequest;

    private List<PayrollRequest> correctRequestsList;

    private UserRequest correctUserRequest;

    @BeforeEach
    void setUp() {
        correctPayrollRequest = PayrollRequest.builder()
                .salary(100)
                .period(YearMonth.now())
                .employeeEmail(EMAIL)
                .build();
        correctRequestsList = List.of(correctPayrollRequest);
        correctUserRequest = UserRequest.builder()
                .name(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Test
    @DisplayName("Correct payrolls pass")
    void correctPayrolls() {
        userDetailsService.signUp(correctUserRequest);
        assertDoesNotThrow(() -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("Payroll for a non-existing user fails")
    void notAUser() {
        correctPayrollRequest.setEmployeeEmail("wrong@man.com");
        assertThrows(UserDoesNotExistException.class, () -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("Payroll list with multiple salaries for one user fails")
    void nonUniquePeriodUserPair() {
        correctRequestsList = List.of(correctPayrollRequest, correctPayrollRequest);
        assertThrows(UserHasMultipleSalariesException.class, () -> payrollService.uploadPayrolls(correctRequestsList));
    }
}
