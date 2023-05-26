package com.example.AccountService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserHasMultipleSalariesException extends RuntimeException {

    public UserHasMultipleSalariesException(String userEmail) {
        super("User %s already has a payroll".formatted(userEmail));
    }

}
