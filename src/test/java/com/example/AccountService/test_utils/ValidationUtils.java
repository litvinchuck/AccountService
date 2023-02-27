package com.example.AccountService.test_utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtils {

    private static final Validator validator;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }


    public static void assertNoValidationViolations(Object dto) {
        assertTrue(validator.validate(dto).isEmpty());
    }

    public static void assertNValidationViolations(int n, Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        assertEquals(n, violations.size());
    }

    public static void assertOneValidationViolation(Object dto) {
        assertNValidationViolations(1, dto);
    }

}
