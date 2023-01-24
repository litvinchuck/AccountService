package com.example.AccountService.services;

import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.exceptions.UserAlreadyExistsException;
import com.example.AccountService.models.Role;
import com.example.AccountService.models.User;
import com.example.AccountService.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> {
            logger.info("user for username {} not found", username);
            throw new UsernameNotFoundException("Not found: " + username);
        });
    }

    public UserResponse signUp(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            logger.info("user for username {} already exists", userRequest.getEmail());
            throw new UserAlreadyExistsException("user %s already exists".formatted(userRequest.getEmail()));
        }
        User user = mapToUser(userRequest);
        userRepository.save(user);
        logger.info("user {} signed up", user);
        return modelMapper.map(user, UserResponse.class);
    }

    public User mapToUser(UserRequest userRequest) {
        Provider<User> userProvider = p -> User.builder().build();
        TypeMap<UserRequest, User> userTypeMap = modelMapper.createTypeMap(UserRequest.class, User.class);
        userTypeMap.setProvider(userProvider);

        User user = userTypeMap.map(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.grantAuthority(Role.ROLE_USER);

        return user;
    }

}
