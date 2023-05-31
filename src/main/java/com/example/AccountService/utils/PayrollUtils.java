package com.example.AccountService.utils;

import com.example.AccountService.models.Payroll;

public class PayrollUtils {

    private static final String formatString = "%d dollar(s) %d cent(s)";

    public static String getFormatString() {
        return formatString;
    }

    public static String generateSalaryString(Payroll payroll) {
//        int dollars = payroll.getSalary() / 100;
//        return "%d dollar(s) %d cent(s)".formatted(dollars, cents);
        return "%d dollar(s) %d cent(s)";
    }
}
