package com.example.AccountService.repositories;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.example.AccountService.models.User;

@Component
public class UserRepository {

    private static long id = 1;

    final private Map<String, User> users = new ConcurrentHashMap<>();

    public User findUserByUsername(String username) {
        return users.get(username);
    }

    public void save(User user) {
        user.setId(id);
        users.put(user.getUsername(), user);
        id += 1;
    }

}
