package com.example.AccountService.services;

import com.example.AccountService.dto.BasicResponse;
import com.example.AccountService.dto.payroll.PayrollRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final ModelMapper modelMapper;

    @Autowired
    public PayrollService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BasicResponse uploadPayrolls(List<PayrollRequest> payrolls) {
        return BasicResponse.builder().status("OK").build();
    }

    public BasicResponse updatePayrollById(Long id, PayrollRequest payroll) {
        return BasicResponse.builder().status("OK").build();
    }

}
