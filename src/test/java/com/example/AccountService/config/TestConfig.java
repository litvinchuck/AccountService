package com.example.AccountService.config;

import com.example.AccountService.test_utils.UserUtilsBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserUtilsBean getUserUtilsBean() {
        return new UserUtilsBean();
    }

}
