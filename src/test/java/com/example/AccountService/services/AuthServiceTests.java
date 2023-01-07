package com.example.AccountService.services;

import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthService authService;

    private UserRequest correctUserRequest;

    private UserResponse correctUserResponse;

    @BeforeEach
    void setUp() {
        correctUserRequest = UserRequest.builder()
                .name("John")
                .lastName("Doe")
                .email("jdoe@acme.com")
                .password("password")
                .build();

        correctUserResponse = UserResponse.builder()
                .name(correctUserRequest.getName())
                .lastName(correctUserRequest.getLastName())
                .email(correctUserRequest.getEmail())
                .build();
    }

    @Test
    @DisplayName("Test sign up with correct data returns correct result")
    void testSignUpWithCorrectData() {
        when(modelMapper.map(correctUserRequest, UserResponse.class)).thenReturn(correctUserResponse);
        assertThat(authService.signUp(correctUserRequest)).isEqualTo(correctUserResponse);
    }

}
