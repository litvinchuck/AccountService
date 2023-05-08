package com.example.AccountService.controllers;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.config.TestConfig;
import com.example.AccountService.dto.BasicResponse;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;
import java.util.List;

import static com.example.AccountService.test_utils.TestConstants.UserDetails.EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PayrollControllerTests {

    //TODO:
    //TODO: Test month-year formats for POST and PUT (test wrong ones and test right one)

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;

    private PayrollRequest payrollRequest;

    private List<PayrollRequest> requestsList;

    private BasicResponse okResponse;

    @BeforeEach
    void setUp() {
        payrollRequest = PayrollRequest.builder()
                .salary(100)
                .period(YearMonth.now())
                .employeeEmail(EMAIL)
                .build();
        requestsList = List.of(payrollRequest);
        okResponse = BasicResponse.builder()
                .status("OK")
                .build();
    }

    @Test
    @DisplayName("Uploading correct payrolls")
    void uploadPayrolls() throws Exception {
        mockMvc.perform(post("/api/acct/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestsList)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(okResponse)));
    }

    @Test
    @DisplayName("Uploading correct payrolls in JSON format")
    void uploadJSONPayrolls() {

    }

    @Test
    @DisplayName("Updating using a correct payroll")
    void updatePayroll() {

    }

    @Test
    @DisplayName("Updating using a correct payroll in JSON format")
    void updateJSONPayroll() {

    }

    @Test
    @DisplayName("Uploading payrolls for a non-existing user")
    void uploadNonExistingUserPayrolls() {

    }

    @Test
    @DisplayName("Uploading multiple payrolls for one user")
    void uploadMultipleSalaries() {

    }

    @Test
    @DisplayName("Updating payroll with non-existing user email")
    void updateForNonExistingUser() {

    }

    @Test
    @DisplayName("Updating payroll so that user gets multiple salaries")
    void updateMultipleSalaries() {

    }

    @Test
    @DisplayName("Uploading payrolls in JSON with wrong date format")
    void uploadJSONBadDateFormat() {

    }

    @Test
    @DisplayName("Updating payroll in JSON with wrong date format")
    void updateJSONBadDateFormat() {

    }
}
