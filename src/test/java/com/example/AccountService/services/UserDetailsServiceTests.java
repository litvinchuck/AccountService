package com.example.AccountService.services;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.config.TestConfig;
import com.example.AccountService.dto.ChangePasswordResponse;
import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.exceptions.BreachedPasswordException;
import com.example.AccountService.exceptions.SamePasswordException;
import com.example.AccountService.exceptions.UserAlreadyExistsException;
import com.example.AccountService.models.Role;
import com.example.AccountService.repositories.BreachedPasswordRepository;
import com.example.AccountService.test_utils.UserUtilsBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.AccountService.models.User;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AccountServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDetailsServiceTests {

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BreachedPasswordRepository breachedPasswordRepository;

    @Autowired
    private UserUtilsBean userUtilsComponent;

    private UserRequest correctUserRequest;

    private User correctUser;

    private UserResponse correctUserResponse;

    private ChangePasswordResponse correctChangePassResponse;

    private static final String PASSWORD = "secret_password";

    private static final String NEW_PASSWORD = "new_secret_password";

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

        correctChangePassResponse = ChangePasswordResponse.builder()
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
    @DisplayName("Test load user by UPPER case email loads correct user")
    void testLoadByUsernameMixedCase() {
        userDetailsService.signUp(correctUserRequest);
        assertThat(userDetailsService.loadUserByUsername(correctUserRequest.getEmail().toUpperCase()))
                .isEqualTo(correctUser);
    }

    @Test
    @DisplayName("Test sign up with correct data returns correct result")
    void testSignUpWithCorrectData() {
        assertThat(userDetailsService.signUp(correctUserRequest)).isEqualTo(correctUserResponse);
    }

    @Test
    @DisplayName("Test sign up with upper case email store email in lower case")
    void testSignUpEmailUpper() {
        correctUserRequest.setEmail(correctUserRequest.getEmail().toUpperCase());
        assertThat(userDetailsService.signUp(correctUserRequest)).isEqualTo(correctUserResponse);
    }

    @Test
    @DisplayName("Test sign up with repeating data throws UserAlreadyExistsException")
    void testSignUpRepeat() {
        userDetailsService.signUp(correctUserRequest);
        assertThrows(UserAlreadyExistsException.class, () -> userDetailsService.signUp(correctUserRequest));
    }

    @Test
    @DisplayName("Test sign up user with repeating email with different case throws UserAlreadyExistsException")
    void testSignUpEmailCase() {
        userDetailsService.signUp(correctUserRequest);
        correctUserRequest.setEmail(correctUserRequest.getEmail().toUpperCase());
        assertThrows(UserAlreadyExistsException.class, () -> userDetailsService.signUp(correctUserRequest));
    }

    @Test
    @DisplayName("Sign up user with breached password throws BreachedPasswordException")
    void breachedSignUp() {
        correctUserRequest.setPassword(userUtilsComponent.getBreachedPassword());
        assertThrows(BreachedPasswordException.class, () -> userDetailsService.signUp(correctUserRequest));
    }

    @Test
    @DisplayName("Test changepass with correct new password")
    void correctChangePass() {
        setUpUser();
        assertThat(userDetailsService.changePass(correctUser, NEW_PASSWORD)).isEqualTo(correctChangePassResponse);
    }

    @Test
    @DisplayName("Test changepass with same password throws SamePasswordException")
    void sameChangePass() {
        setUpUser();
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
        assertThrows(SamePasswordException.class, () -> userDetailsService.changePass(correctUser, PASSWORD));
    }

    @Test
    @DisplayName("Test changepass with breached password throws BreachedPasswordException")
    void breachedTestPass() {
        setUpUser();
        assertThrows(BreachedPasswordException.class,
                () -> userDetailsService.changePass(correctUser, userUtilsComponent.getBreachedPassword()));
    }

    private void setUpUser() {
        userDetailsService.signUp(correctUserRequest);
    }

}
