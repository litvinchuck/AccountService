package com.example.AccountService.dto.payroll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRequest {

    private long salary;

    private YearMonth period;

    @JsonProperty("employee")
    private String employeeEmail;

}
