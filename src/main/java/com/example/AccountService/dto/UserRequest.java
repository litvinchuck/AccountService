package com.example.AccountService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "name property should not be blank")
    private String name;

    @NotBlank(message = "lastname property should not be blank")
    @JsonProperty("lastname")
    private String lastName;

    @NotBlank(message = "email property should not be blank")
    @Email(regexp = ".+@acme\\.com$")
    private String email;

    @NotBlank(message = "password property should not be blank")
    private String password;

}
