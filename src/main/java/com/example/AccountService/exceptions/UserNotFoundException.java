package com.example.AccountService.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    public UserNotFoundException(String username) {
        super("Not found: " + username);
    }
}
