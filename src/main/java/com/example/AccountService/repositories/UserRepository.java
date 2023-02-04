package com.example.AccountService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.example.AccountService.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
