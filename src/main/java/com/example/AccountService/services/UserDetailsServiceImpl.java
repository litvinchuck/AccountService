package com.example.AccountService.services;

import com.example.AccountService.dto.ChangePasswordResponse;
import com.example.AccountService.dto.UserRequest;
import com.example.AccountService.dto.UserResponse;
import com.example.AccountService.exceptions.BreachedPasswordException;
import com.example.AccountService.exceptions.SamePasswordException;
import com.example.AccountService.exceptions.UserAlreadyExistsException;
import com.example.AccountService.models.Role;
import com.example.AccountService.models.User;
import com.example.AccountService.repositories.BreachedPasswordRepository;
import com.example.AccountService.repositories.UserRepository;
import org.modelmapper.ModelMapper;
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

    private final UserRepository userRepository;

    private final BreachedPasswordRepository breachedPasswordRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final TypeMap<UserRequest, User> userTypeMap;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  BreachedPasswordRepository breachedPasswordRepository,
                                  PasswordEncoder passwordEncoder,
                                  ModelMapper modelMapper,
                                  TypeMap<UserRequest, User> userTypeMap) {
        this.userRepository = userRepository;
        this.breachedPasswordRepository = breachedPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userTypeMap = userTypeMap;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username.toLowerCase()).orElseThrow(() -> {
            logger.info("user for username {} not found", username);
            throw new UsernameNotFoundException("Not found: " + username);
        });
    }

    public UserResponse signUp(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail().toLowerCase())) {
            logger.info("user for username {} already exists", userRequest.getEmail());
            throw new UserAlreadyExistsException("user %s already exists".formatted(userRequest.getEmail()));
        }
        if (breachedPasswordRepository.existsByPassword(userRequest.getPassword())) {
            logger.info("user for username {} uses a breached password", userRequest.getEmail());
            throw new BreachedPasswordException("used password is breached");
        }

        User user = userTypeMap.map(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.grantAuthority(Role.ROLE_USER);
        userRepository.save(user);
        logger.info("user {} signed up", user);

        return modelMapper.map(user, UserResponse.class);
    }

    public ChangePasswordResponse changePass(UserDetails user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException("The passwords must be different!");
        }
        if (breachedPasswordRepository.existsByPassword(newPassword)) {
            throw new BreachedPasswordException("The password is in the hacker's database!");
        }

        User model = modelMapper.map(user, User.class);
        userRepository.save(model);
        return modelMapper.map(user, ChangePasswordResponse.class);
    }

}
