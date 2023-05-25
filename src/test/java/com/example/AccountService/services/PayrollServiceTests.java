package com.example.AccountService.services;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.example.AccountService.dto.user_request.UserRequest;
import com.example.AccountService.exceptions.UserHasMultipleSalariesException;
import com.example.AccountService.models.Payroll;
import com.example.AccountService.repositories.PayrollRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

import java.time.YearMonth;
import java.util.List;

import static com.example.AccountService.test_utils.TestConstants.UserDetails.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AccountServiceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PayrollServiceTests {
    @Autowired
    private PayrollService payrollService;
    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private final PayrollRequest correctPayrollRequest = PayrollRequest.builder()
            .salary(100L)
            .period(YearMonth.now())
            .employeeEmail(EMAIL)
            .build();
    private final PayrollRequest anotherCorrectRequest = PayrollRequest.builder()
            .salary(100L)
            .period(YearMonth.now())
            .employeeEmail(ANOTHER_EMAIL)
            .build();
    private final PayrollRequest wrongPayrollRequest = PayrollRequest.builder()
            .salary(100L)
            .period(YearMonth.now())
            .employeeEmail(WRONG_EMAIL)
            .build();
    private List<PayrollRequest> correctRequestsList = List.of(correctPayrollRequest, anotherCorrectRequest);
    private final UserRequest correctUserRequest = UserRequest.builder()
            .name(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .build();
    private final UserRequest anotherCorrectUserRequest = UserRequest.builder()
            .name(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(ANOTHER_EMAIL)
            .password(PASSWORD)
            .build();
    private final Payroll correctPayroll = Payroll.builder()
            .id(1L)
            .salary(correctPayrollRequest.getSalary())
            .period(correctPayrollRequest.getPeriod())
            .build();
    private final Payroll anotherCorrectPayroll = Payroll.builder()
            .id(2L)
            .salary(anotherCorrectRequest.getSalary())
            .period(anotherCorrectRequest.getPeriod())
            .build();
    private static final String WRONG_EMAIL = "wrong@man.com";

    @BeforeEach
    void setUp() {
        signUpUsers();
    }

    @Test
    @DisplayName("POSTing correct payrolls passes")
    void postCorrectPayrolls() {
        assertDoesNotThrow(() -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("POSTing payrolls for a non-existing user fails")
    void notAUser() {
        correctPayrollRequest.setEmployeeEmail(WRONG_EMAIL);
        assertThrows(UsernameNotFoundException.class, () -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("Throwing UsernameNotFoundException during POSTing rolls back the transaction")
    void notAUserRollsBack() {
        anotherCorrectRequest.setEmployeeEmail(WRONG_EMAIL);
        assertThat(payrollRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Payroll list with multiple salaries for one user fails")
    void nonUniquePeriodUserPair() {
        correctRequestsList = List.of(correctPayrollRequest, correctPayrollRequest);
        assertThrows(UserHasMultipleSalariesException.class, () -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("Throwing UserHasMultipleSalariesException during POSTing rolls back the transaction")
    void nonUniquePeriodUserPairRollsBack() {
        nonUniquePeriodUserPair();
        assertThat(payrollRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Updating a correct payroll passes")
    void updateWithCorrectPayroll() {
        payrollService.uploadPayrolls(correctRequestsList);
        assertDoesNotThrow(() -> payrollService.updatePayrollById(1L, correctPayrollRequest));
    }

    @Test
    @DisplayName("Updating a payroll for a non-existing user fails")
    void updateNotAUser() {
        payrollService.uploadPayrolls(correctRequestsList);
        assertThrows(UsernameNotFoundException.class,
                () -> payrollService.updatePayrollById(1L, wrongPayrollRequest));
    }

    @Test
    @DisplayName("Throwing UserDoesNotExistException during update rolls back the transaction")
    void updateNotAUserRollsBack() {
        updateNotAUser();
        assertThat(payrollRepository.getReferenceById(1L)).isEqualTo(correctPayroll);
        assertThat(payrollRepository.getReferenceById(2L)).isEqualTo(anotherCorrectPayroll);
    }

    @Test
    @DisplayName("Updating the payroll so that a person has multiple salaries fails")
    void updateNonUniquePeriodUserPair() {
        payrollService.uploadPayrolls(correctRequestsList);

        assertThrows(UserHasMultipleSalariesException.class,
                () -> payrollService.updatePayrollById(2L, correctPayrollRequest));
    }

    @Test
    @DisplayName("Throwing UserHasMultipleSalariesException during the update rolls back the transaction")
    void updateNonUniquePeriodUserPairRollsBack() {
        updateNonUniquePeriodUserPair();
        assertThat(payrollRepository.getReferenceById(1L)).isEqualTo(correctPayroll);
        assertThat(payrollRepository.getReferenceById(2L)).isEqualTo(anotherCorrectPayroll);
    }

    private void signUpUsers() {
        userDetailsService.signUp(correctUserRequest);
        userDetailsService.signUp(anotherCorrectUserRequest);
    }
}
