package com.example.AccountService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import com.example.AccountService.models.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

}
