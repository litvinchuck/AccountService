package com.example.AccountService.utils;

import com.example.AccountService.models.Payroll;

public class PayrollUtils {

    private static final String formatString = "%d dollar(s) %d cent(s)";

    public static String getFormatString() {
        return formatString;
    }

    public static String generateSalaryString(Payroll payroll) {
        return getFormatString()
                .formatted(payroll.getSalary() / 100, payroll.getSalary() % 100);
    }
}
