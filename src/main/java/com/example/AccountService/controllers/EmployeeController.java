package com.example.AccountService.controllers;

import com.example.AccountService.dto.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/empl")
public class EmployeeController {

    private ModelMapper modelMapper;

    @Autowired
    public EmployeeController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("payment")
    public UserResponse payment(Authentication auth) {
        return modelMapper.map(auth.getPrincipal(), UserResponse.class);
    }

}
