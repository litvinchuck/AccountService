package com.example.AccountService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;

    private String username;

    private String role;

    private String name;

    private String lastName;

    private String email;

    private String password;

}
