package com.example.AccountService.dto.payroll;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @PositiveOrZero(message = "salary property can not be less than 0")
    private Long salary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-yyyy")
    private YearMonth period;

    @JsonProperty("employee")
    @NotBlank(message = "employee property should not be blank")
    @Email(regexp = ".+@acme\\.com$", message = "email should be registered in acme.com domain")
    private String employeeEmail;

}
