package com.example.AccountService.test_utils.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponseInfo {

    private Integer status;
    private String error;
    private String path;
}
