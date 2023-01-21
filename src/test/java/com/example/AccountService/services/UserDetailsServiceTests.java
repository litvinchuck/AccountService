package com.example.AccountService.services;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.exception.UserAlreadyExistsException;
import com.example.AccountService.models.Role;
import com.example.AccountService.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.AccountService.models.User;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDetailsServiceTests {

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private UserRequest correctUserRequest;

    private User correctUser;

    private UserResponse correctUserResponse;

    private static final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);

        correctUserRequest = UserRequest.builder()
                .name("John")
                .lastName("Doe")
                .email("jdoe@acme.com")
                .password(PASSWORD)
                .build();

        correctUser = User.builder()
                .id(1L)
                .name(correctUserRequest.getName())
                .lastName(correctUserRequest.getLastName())
                .email(correctUserRequest.getEmail())
                .password(correctUserRequest.getPassword())
                .build();
        correctUser.grantAuthority(Role.ROLE_USER);

        correctUserResponse = UserResponse.builder()
                .id(1L)
                .name(correctUserRequest.getName())
                .lastName(correctUserRequest.getLastName())
                .email(correctUserRequest.getEmail())
                .build();
    }

    @Test
    @DisplayName("Test load user by username with existing user works correctly")
    void testLoadByUsernameCorrect() {
        userDetailsService.signUp(correctUserRequest);
        assertThat(userDetailsService.loadUserByUsername(correctUserRequest.getEmail())).isEqualTo(correctUser);
    }

    @Test
    @DisplayName("Test load user by username with non-existing user throws UsernameNotFoundException")
    void testLoadByUsernameNotFound() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(correctUserRequest.getEmail()));
    }

    @Test
    @DisplayName("Test sign up with correct data returns correct result")
    void testSignUpWithCorrectData() {
        assertThat(userDetailsService.signUp(correctUserRequest)).isEqualTo(correctUserResponse);
    }

    @Test
    @DisplayName("Test sign up with repeating data throws UserAlreadyExistsException")
    void testSignUpRepeat() {
        userDetailsService.signUp(correctUserRequest);
        assertThrows(UserAlreadyExistsException.class, () -> userDetailsService.signUp(correctUserRequest));
    }

}
