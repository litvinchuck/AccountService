package com.example.AccountService.services;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.config.TestConfig;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.example.AccountService.dto.user_request.UserRequest;
import com.example.AccountService.exceptions.UserDoesNotExistException;
import com.example.AccountService.exceptions.UserHasMultipleSalariesException;
import com.example.AccountService.models.Payroll;
import com.example.AccountService.repositories.PayrollRepository;
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
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PayrollServiceTests {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private PayrollRequest correctPayrollRequest;

    private PayrollRequest anotherCorrectRequest;

    private List<PayrollRequest> correctRequestsList;

    private UserRequest correctUserRequest;

    private Payroll correctPayroll;

    private Payroll anotherCorrectPayroll;

    private static final String WRONG_EMAIL = "wrong@man.com";

    @BeforeEach
    void setUp() {
        correctPayrollRequest = PayrollRequest.builder()
                .salary(100)
                .period(YearMonth.now())
                .employeeEmail(EMAIL)
                .build();
        anotherCorrectRequest = PayrollRequest.builder()
                .salary(100)
                .period(YearMonth.now())
                .employeeEmail("another" + EMAIL)
                .build();
        correctRequestsList = List.of(correctPayrollRequest, anotherCorrectRequest);
        correctUserRequest = UserRequest.builder()
                .name(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        correctPayroll = Payroll.builder()
                .id(1L)
                .salary(correctPayrollRequest.getSalary())
                .period(correctPayrollRequest.getPeriod())
                .build();
        anotherCorrectPayroll = Payroll.builder()
                .id(2L)
                .salary(anotherCorrectRequest.getSalary())
                .period(anotherCorrectRequest.getPeriod())
                .build();
    }

    @Test
    @DisplayName("POSTing correct payrolls passes")
    void postCorrectPayrolls() {
        userDetailsService.signUp(correctUserRequest);
        assertDoesNotThrow(() -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("POSTing payrolls for a non-existing user fails")
    void notAUser() {
        correctPayrollRequest.setEmployeeEmail(WRONG_EMAIL);
        assertThrows(UserDoesNotExistException.class, () -> payrollService.uploadPayrolls(correctRequestsList));
    }

    @Test
    @DisplayName("Throwing UserDoesNotExistException during POSTing rolls back the transaction")
    void notAUserRollsBack() {
        notAUser();
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
        userDetailsService.signUp(correctUserRequest);
        payrollService.uploadPayrolls(correctRequestsList);
        assertDoesNotThrow(() -> payrollService.updatePayrollById(1L, correctPayrollRequest));
    }

    @Test
    @DisplayName("Updating a payroll for a non-existing user fails")
    void updateNotAUser() {
        correctPayrollRequest.setEmployeeEmail(WRONG_EMAIL);
        payrollService.uploadPayrolls(correctRequestsList);

        assertThrows(UserDoesNotExistException.class,
                () -> payrollService.updatePayrollById(1L, correctPayrollRequest));
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
}
