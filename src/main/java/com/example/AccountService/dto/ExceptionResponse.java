package com.example.AccountService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {

    private int status;
    private String error;
    private LocalDateTime timestamp;
    private String message;
    private String path;

}
