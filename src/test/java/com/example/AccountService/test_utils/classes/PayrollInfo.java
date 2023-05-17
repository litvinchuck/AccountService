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
public class PayrollInfo {

    @JsonProperty("employee")
    private String employeeEmail;

    private Long salary;

    private String period;
}
