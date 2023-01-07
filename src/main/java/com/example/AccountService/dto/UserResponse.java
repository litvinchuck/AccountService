package com.example.AccountService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @NotBlank
    private String name;

    @NotBlank
    @JsonProperty("lastname")
    private String lastName;

    @NotBlank
    @Email(regexp = ".+@acme\\.com$")
    private String email;

}
