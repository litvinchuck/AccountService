package com.example.AccountService.controllers;

import com.example.AccountService.AccountServiceApplication;
import com.example.AccountService.BaseSpringTest;
import com.example.AccountService.test_utils.MVCUtils;
import com.example.AccountService.test_utils.UserUtils;
import com.example.AccountService.test_utils.classes.ChangePasswordInfo;
import com.example.AccountService.test_utils.classes.UserInfo;
import com.example.AccountService.test_utils.classes.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;

import static com.example.AccountService.test_utils.MVCUtils.convertToJSONString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.example.AccountService.test_utils.TestConstants.UserDetails.*;

@SpringBootTest(classes = AccountServiceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTests extends BaseSpringTest {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private MVCUtils mvcUtils;
    private final UserInfo userInfo = UserInfo.builder()
            .name(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .build();
    private final UserResponse userResponse = UserResponse.builder()
            .id(1L)
            .name(userInfo.getName())
            .lastName(userInfo.getLastName())
            .email(userInfo.getEmail())
            .build();
    private final ChangePasswordInfo changePasswordInfo = ChangePasswordInfo.builder()
            .newPassword(NEW_PASSWORD)
            .build();
    private final ChangePasswordInfo breachedPasswordInfo = ChangePasswordInfo.builder()
            .build();
    private final String signupApi = "/api/auth/signup";
    private final String testApi = "/api/auth/test";
    private final String changePassApi = "/api/auth/changepass";

    @BeforeEach
    void setUp() {
        breachedPasswordInfo.setNewPassword(userUtils.getBreachedPassword());
    }

    @Test
    @DisplayName("Signing up a new user returns status 200 and correct json")
    void testSignUpWithNewUser() throws Exception {
        mvcUtils.performPost(signupApi, userInfo)
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJSONString(userResponse)));
    }

    @Test
    @DisplayName("Signing up duplicate user returns status 400")
    void testSignUpDuplicate() throws Exception {
        setUpUser();
        mvcUtils.performPost(signupApi, userInfo)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Accessing api/auth/test endpoint without signing in returns 200")
    void testTestEndpointWithAuth() throws Exception {
        mvcUtils.performGet(testApi)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Accessing api/auth/test endpoint without signing in returns 401")
    void testTestEndpointWithoutAuth() throws Exception {
        mvcUtils.performGet(testApi)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @WithUserDetails
    @DisplayName("Change pass authorized returns 200")
    void testChangePassAuth() throws Exception {
        mvcUtils.performPost(changePassApi, changePasswordInfo)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Change pass unauthorized returns 401")
    void testChangePassUnAuth() throws Exception {
        mvcUtils.performPost(changePassApi, changePasswordInfo)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @WithUserDetails
    @DisplayName("Change pass with breached password returns 400")
    void testChangePassBreached() throws Exception {
        mvcUtils.performPost(changePassApi, breachedPasswordInfo)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER", password = NEW_PASSWORD_HASH)
    @WithUserDetails
    @DisplayName("Change pass with same password returns 400")
    void testChangePassSame() throws Exception {
        mvcUtils.performPost(changePassApi, changePasswordInfo)
                .andExpect(status().isBadRequest());
    }

    private void setUpUser() throws Exception {
        mvcUtils.performPost(signupApi, userInfo);
    }
}
