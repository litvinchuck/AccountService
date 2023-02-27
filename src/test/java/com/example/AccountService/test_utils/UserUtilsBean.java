package com.example.AccountService.test_utils;

import com.example.AccountService.models.BreachedPassword;
import com.example.AccountService.repositories.BreachedPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class UserUtilsBean {

    @Autowired
    private BreachedPasswordRepository breachedPasswordRepository;

    public String getBreachedPassword() {
        List<BreachedPassword> passwords = breachedPasswordRepository.findAll();
        Collections.shuffle(passwords);
        return passwords.get(0).getPassword();
    }

}
