package com.example.AccountService.utils;

import com.example.AccountService.models.Payroll;
import com.example.AccountService.models.User;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

public class PayrollUtilsTests {
    private final Payroll payroll = Payroll.builder()
            .id(1)
            .salary(0)
            .period(YearMonth.now())
            .user(User.builder().build())
            .build();

    @Test
    void testOneDollar() {
        payroll.setSalary(100);
        assertCorrectConversionToString(payroll, 1, 0);
    }

    @Test
    void testZeroCents() {
        payroll.setSalary(0);
        assertCorrectConversionToString(payroll, 0, 0);
    }

    @Test
    void testFiveCents() {
        payroll.setSalary(5);
        assertCorrectConversionToString(payroll, 0, 5);
    }

    @Test
    void testThirtyFiveCents() {
        payroll.setSalary(35);
        assertCorrectConversionToString(payroll, 0, 35);
    }

    @Test
    void testBigNumber() {
        payroll.setSalary(56789);
        assertCorrectConversionToString(payroll, 567, 89);
    }

    @Test
    void testMaximumValue() {
        payroll.setSalary(Long.MAX_VALUE);
        assertCorrectConversionToString(payroll, 92233720368547758L, 7);
    }

    private void assertCorrectConversionToString(Payroll payroll, long dollars, int cents) {
        assertThat(PayrollUtils.generateSalaryString(payroll))
                .isEqualTo(PayrollUtils.getFormatString().formatted(dollars, cents));
    }
}
