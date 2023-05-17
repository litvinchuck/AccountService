package com.example.AccountService.test_utils.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private String name;

    @JsonProperty("lastname")
    private String lastName;

    private String email;

    private String password;
}
