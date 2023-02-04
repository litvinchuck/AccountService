package com.example.AccountService.config;

import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.models.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TypeMap<UserRequest, User> getUserTypeMap() {
        Provider<User> userProvider = p -> User.builder().build();
        TypeMap<UserRequest, User> userTypeMap = getModelMapper().createTypeMap(UserRequest.class, User.class);
        userTypeMap.setProvider(userProvider);

        return userTypeMap;
    }

}
