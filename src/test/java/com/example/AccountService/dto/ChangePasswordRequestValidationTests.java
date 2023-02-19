package com.example.AccountService.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.AccountService.test_utils.ValidationUtils.assertNoValidationViolations;
import static com.example.AccountService.test_utils.ValidationUtils.assertOneValidationViolation;

public class ChangePasswordRequestValidationTests {

    private ChangePasswordRequest correctChangePasswordRequest;

    private static final String SHORT_PASSWORD = "password";

    private static final String EXACT_PASSWORD = "babyproofing";

    @BeforeEach
    void setUpBeforeEach() {
        correctChangePasswordRequest = ChangePasswordRequest.builder()
                .password("secret_password")
                .build();
    }

    @Test
    @DisplayName("Test changepass with correct password")
    void correctChangePass() {
        assertNoValidationViolations(correctChangePasswordRequest);
    }

    @Test
    @DisplayName("Test changepass with password less than 12 symbols")
    void shortChangePass() {
        correctChangePasswordRequest.setPassword(SHORT_PASSWORD);
        assertOneValidationViolation(correctChangePasswordRequest);
    }

    @Test
    @DisplayName("Test changepass with password exactly 12 symbols")
    void exactChangePass() {
        correctChangePasswordRequest.setPassword(EXACT_PASSWORD);
        assertNoValidationViolations(correctChangePasswordRequest);
    }

    @Test
    @DisplayName("Test changepass with null password")
    void nullChangePass() {
        correctChangePasswordRequest.setPassword(null);
        assertOneValidationViolation(correctChangePasswordRequest);
    }

}
