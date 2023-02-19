package com.example.AccountService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Email(regexp = ".+@acme\\.com$", message = "email should be registered in acme.com domain")
    private String email;

    @NotNull(message = "password property must be present")
    @Size(min=12)
    private String password;

}
