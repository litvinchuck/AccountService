package com.example.AccountService.dto;

import com.example.AccountService.dto.user_request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.AccountService.test_utils.ValidationUtils.*;

public class UserRequestValidationTests {

    private UserRequest correctUserRequest;

    private static final String SHORT_PASSWORD = "password";

    private static final String EXACT_PASSWORD = "babyproofing";

    @BeforeEach
    void setUpBeforeEach() {
        correctUserRequest = UserRequest.builder()
                .name("John")
                .lastName("Doe")
                .email("jdoe@acme.com")
                .password("secret_password")
                .build();
    }

    @Test
    @DisplayName("Correct UserRequest passes validation")
    void correctUserRequestValidates() {
        assertNoValidationViolations(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with null name has one validation violation")
    void nullName() {
        correctUserRequest.setName(null);
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with empty name has one validation violation")
    void emptyName() {
        correctUserRequest.setName("");
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with null lastName has one validation violation")
    void nullLastName() {
        correctUserRequest.setLastName(null);
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with empty lastName has one validation violation")
    void emptyLastName() {
        correctUserRequest.setLastName("");
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with null email has one validation violation")
    void nullEmail() {
        correctUserRequest.setEmail(null);
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with empty email has two validation violations")
    void emptyEmail() {
        correctUserRequest.setEmail("");
        assertNValidationViolations(2, correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with correct email but wrong domain has one validation violation")
    void correctEmailWrongDomain() {
        correctUserRequest.setEmail("jdoe@gmail.com");
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with wrong email but correct domain has one validation violation")
    void wrongEmailCorrectDomain() {
        correctUserRequest.setEmail("A@b@c@acme.com");
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with null password has one validation violation")
    void nullPassword() {
        correctUserRequest.setPassword(null);
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with empty password has one validation violation")
    void emptyPassword() {
        correctUserRequest.setPassword("");
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with a password shorter than 12 is rejected")
    void tooShortPassword() {
        correctUserRequest.setPassword(SHORT_PASSWORD);
        assertOneValidationViolation(correctUserRequest);
    }

    @Test
    @DisplayName("UserRequest with password length of precisely 12 is accepted")
    void exactPassword() {
        correctUserRequest.setPassword(EXACT_PASSWORD);
        assertNoValidationViolations(correctUserRequest);
    }

}
