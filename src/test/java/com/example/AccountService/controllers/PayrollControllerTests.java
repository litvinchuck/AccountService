package com.example.AccountService.controllers;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.BaseSpringTest;
import com.example.AccountService.test_utils.MVCUtils;
import com.example.AccountService.test_utils.classes.BasicResponseInfo;
import com.example.AccountService.test_utils.classes.ExceptionResponseInfo;
import com.example.AccountService.test_utils.classes.PayrollInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static com.example.AccountService.test_utils.MVCUtils.convertToJSONString;
import static com.example.AccountService.test_utils.TestConstants.Payrolls.CORRECT_PERIOD;
import static com.example.AccountService.test_utils.TestConstants.Payrolls.WRONG_PERIOD;
import static com.example.AccountService.test_utils.TestConstants.UserDetails.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AccountServiceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PayrollControllerTests extends BaseSpringTest {
    @Autowired
    private MVCUtils mvcUtils;
    private final String postPaymentApi = "/api/acct/payments";
    private final String putPaymentApi = "/api/acct/payments/{id}";
    private final PayrollInfo correctPayrollInfo = PayrollInfo.builder()
            .salary(100L)
            .period(CORRECT_PERIOD)
            .employeeEmail(EMAIL)
            .build();
    private final PayrollInfo anotherCorrectPayrollInfo = PayrollInfo.builder()
            .salary(100L)
            .period(CORRECT_PERIOD)
            .employeeEmail(ANOTHER_EMAIL)
            .build();
    private final PayrollInfo nonExistingEmailInfo = PayrollInfo.builder()
            .salary(100L)
            .period(CORRECT_PERIOD)
            .employeeEmail(WRONG_EMAIL)
            .build();
    private final PayrollInfo badDateFormatInfo = PayrollInfo.builder()
            .salary(100L)
            .period(WRONG_PERIOD)
            .employeeEmail(EMAIL)
            .build();
    private final BasicResponseInfo okResponse = BasicResponseInfo.builder()
            .status("OK")
            .build();
    private final ExceptionResponseInfo badRequestResponse = ExceptionResponseInfo.builder()
            .status(400)
            .error("Bad Request")
            .path(postPaymentApi)
            .build();
    private final List<PayrollInfo> correctPayrollInfoList = List.of(correctPayrollInfo, anotherCorrectPayrollInfo);
    private final List<PayrollInfo> multipleSalariesList = List.of(correctPayrollInfo, correctPayrollInfo);
    private final List<PayrollInfo> badDateFormatInfoList = List.of(badDateFormatInfo);

    @Test
    @WithMockUser(username = EMAIL, roles = "USER")
    @DisplayName("Uploading correct payrolls")
    void uploadPayrolls() throws Exception {
        mvcUtils.performPost(postPaymentApi, correctPayrollInfoList)
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJSONString(okResponse)));
    }

    @Test
    @WithMockUser(username = EMAIL, roles = "USER")
    @DisplayName("Updating using a correct payroll")
    void updatePayroll() throws Exception {
        mvcUtils.performPost(postPaymentApi, correctPayrollInfoList);
        mvcUtils.performPut(putPaymentApi, correctPayrollInfo, 1)
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJSONString(okResponse)));
    }

    @Test
    @DisplayName("Uploading payrolls for a non-existing user")
    void uploadNonExistingUserPayrolls() throws Exception {
        mvcUtils.performPost(postPaymentApi, correctPayrollInfoList)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(convertToJSONString(badRequestResponse)));
    }

    @Test
    @DisplayName("Uploading multiple payrolls for one user")
    void uploadMultipleSalaries() throws Exception {
        mvcUtils.performPost(postPaymentApi, multipleSalariesList)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(convertToJSONString(badRequestResponse)));
    }

    @Test
    @DisplayName("Updating payroll with non-existing user email")
    void updateForNonExistingUser() throws Exception {
        mvcUtils.performPost(postPaymentApi, correctPayrollInfoList);
        mvcUtils.performPut(putPaymentApi, nonExistingEmailInfo, 1)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(convertToJSONString(badRequestResponse)));
    }

    @Test
    @DisplayName("Updating payroll so that user gets multiple salaries")
    void updateMultipleSalaries() throws Exception {
        mvcUtils.performPost(postPaymentApi, correctPayrollInfoList);
        mvcUtils.performPut(putPaymentApi, anotherCorrectPayrollInfo, 1)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(convertToJSONString(badRequestResponse)));
    }

    @Test
    @DisplayName("Uploading payrolls with wrong date format")
    void uploadJSONBadDateFormat() throws Exception {
        mvcUtils.performPost(postPaymentApi, badDateFormatInfoList)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(convertToJSONString(badRequestResponse)));
    }

    @Test
    @DisplayName("Updating payroll with wrong date format")
    void updateJSONBadDateFormat() throws Exception {
        mvcUtils.performPost(postPaymentApi, correctPayrollInfoList);
        mvcUtils.performPut(putPaymentApi, badDateFormatInfo, 1)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(convertToJSONString(badRequestResponse)));
    }
}
