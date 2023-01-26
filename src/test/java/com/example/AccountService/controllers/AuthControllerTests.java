package com.example.AccountService.controllers;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.exceptions.UserAlreadyExistsException;
import com.example.AccountService.models.Role;
import com.example.AccountService.models.User;
import com.example.AccountService.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserRequest userRequest;

    private UserResponse userResponse;

    private static final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .name("John")
                .lastName("Doe")
                .email("jdoe@acme.com")
                .password(PASSWORD)
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("jdoe@acme.com")
                .build();
    }

    @Test
    @DisplayName("Signing up a new user returns status 200 and correct json")
    void testSignUpWithNewUser() throws Exception {
        when(userDetailsService.signUp(any())).thenReturn(userResponse);
        mockMvc.perform(post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    @DisplayName("Signing up duplicate user returns status 400")
    void testSignUpDuplicate() throws Exception {
        when(userDetailsService.signUp(any())).thenThrow(UserAlreadyExistsException.class);
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Accessing api/auth/test endpoint without signing in returns 200")
    void testTestEndpointWithAuth() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Accessing api/auth/test endpoint without signing in returns 401")
    void testTestEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isUnauthorized());
    }

}
