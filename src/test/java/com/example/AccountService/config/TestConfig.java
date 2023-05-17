package com.example.AccountService.config;

import com.example.AccountService.repositories.BreachedPasswordRepository;
import com.example.AccountService.test_utils.MVCUtils;
import com.example.AccountService.test_utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
public class TestConfig {

    @Autowired
    private BreachedPasswordRepository breachedPasswordRepository;
    @Autowired
    private MockMvc mockMvc;

    @Bean
    public UserUtils getUserUtilsBean() {
        return new UserUtils(breachedPasswordRepository);
    }

    @Bean
    public MVCUtils getMVCUtils() {
        return new MVCUtils(mockMvc);
    }
}
