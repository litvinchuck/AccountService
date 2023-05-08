package com.example.AccountService.controllers;

import com.example.AccountService.dto.BasicResponse;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.example.AccountService.services.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/acct")
public class PayrollController {

    private PayrollService payrollService;

    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @RequestMapping("payments")
    public BasicResponse uploadPayrolls(@RequestBody List<PayrollRequest> payrolls) {
        return payrollService.uploadPayrolls(payrolls);
    }

    @RequestMapping("payments/{id}")
    public BasicResponse updatePayrollById(@PathVariable Long id, @RequestBody PayrollRequest payroll) {
        return payrollService.updatePayrollById(id, payroll);
    }
}
