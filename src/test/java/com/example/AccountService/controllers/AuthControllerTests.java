package com.example.AccountService.controllers;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.config.TestConfig;
import com.example.AccountService.dto.ChangePasswordRequest;
import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.test_utils.UserUtilsBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserUtilsBean userUtilsBean;
    private ObjectMapper objectMapper = new ObjectMapper();
    private UserRequest userRequest;
    private UserResponse userResponse;
    private ChangePasswordRequest changePasswordRequest;
    private static final String PASSWORD = "secret_password";
    private static final String NEW_PASSWORD = "new_secret_password";
    private static final String NEW_PASSWORD_HASH = "$2a$12$IT3wzbFeWJJaHWNwcy9LQO4CdPjQn8Sf987IdL3M5Wu5ox/yFC4b2";

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

        changePasswordRequest = ChangePasswordRequest.builder()
                .newPassword(NEW_PASSWORD)
                .build();
    }

    @Test
    @DisplayName("Signing up a new user returns status 200 and correct json")
    void testSignUpWithNewUser() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    @DisplayName("Signing up duplicate user returns status 400")
    void testSignUpDuplicate() throws Exception {
        setUpUser();
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

    @Test
    @WithMockUser(roles = "USER")
    @WithUserDetails
    @DisplayName("Change pass authorized returns 200")
    void testChangePassAuth() throws Exception {
        mockMvc.perform(post("/api/auth/changepass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Change pass unauthorized returns 401")
    void testChangePassUnAuth() throws Exception {
        mockMvc.perform(post("/api/auth/changepass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @WithUserDetails
    @DisplayName("Change pass with breached password returns 400")
    void testChangePassBreached() throws Exception {
        changePasswordRequest.setNewPassword(userUtilsBean.getBreachedPassword());

        mockMvc.perform(post("/api/auth/changepass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER", password = NEW_PASSWORD_HASH)
    @WithUserDetails
    @DisplayName("Change pass with same password returns 400")
    void testChangePassSame() throws Exception {
        mockMvc.perform(post("/api/auth/changepass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    private void setUpUser() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));
    }

}
