package com.example.AccountService;

import com.example.AccountService.config.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class BaseSpringTest {
}
