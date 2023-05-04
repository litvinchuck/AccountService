package com.example.AccountService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException(String cause) {
        super(cause);
    }

}
