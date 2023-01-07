package com.example.AccountService.services;

import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final ModelMapper modelMapper;

    @Autowired
    public AuthService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserResponse signUp(UserRequest user) {
        logger.info("user {} signed up", user);
        return modelMapper.map(user, UserResponse.class);
    }

}
