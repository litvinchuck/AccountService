package com.example.AccountService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "username property should not be blank")
    private String username;

    @NotBlank(message = "name property should not be blank")
    private String name;

    @NotBlank(message = "role property should not be blank")
    @Pattern(regexp="^ROLE_\\S+", message = "role must start with 'ROLE_'")
    private String role;

    @NotBlank(message = "lastname property should not be blank")
    @JsonProperty("lastname")
    private String lastName;

    @NotBlank(message = "email property should not be blank")
    @Email(regexp = ".+@acme\\.com$", message = "email should be registered in acme.com domain")
    private String email;

    @NotBlank(message = "password property should not be blank")
    private String password;

}
