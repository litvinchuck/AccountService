package com.example.AccountService.controllers;

import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/test")
    public String test() {
        return "/test is accessed";
    }

    @PostMapping("signup")
    public UserResponse signUp(@RequestBody @Valid UserRequest user) {
        logger.info("POST api/auth/signup body={}", user);
        return userDetailsService.signUp(user);
    }

}
