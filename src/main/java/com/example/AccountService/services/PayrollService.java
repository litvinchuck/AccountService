package com.example.AccountService.services;

import com.example.AccountService.dto.BasicResponse;
import com.example.AccountService.dto.payroll.PayrollRequest;
import com.example.AccountService.exceptions.UserHasMultipleSalariesException;
import com.example.AccountService.models.Payroll;
import com.example.AccountService.models.User;
import com.example.AccountService.repositories.PayrollRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PayrollService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final ModelMapper modelMapper;
    private final PayrollRepository payrollRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public PayrollService(ModelMapper modelMapper, PayrollRepository payrollRepository, UserDetailsServiceImpl userDetailsService) {
        this.modelMapper = modelMapper;
        this.payrollRepository = payrollRepository;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public BasicResponse uploadPayrolls(List<PayrollRequest> payrolls) {
        payrolls.stream()
                .map(this::mapRequestToPayroll)
                .forEach(payroll -> {
                    if (payrollRepository.existsByUserEmail(payroll.getUser().getEmail())) {
                        logger.info("User {} already has a payroll", payroll.getUser().getEmail()); //TODO: refactor exceptions so they have errors in constructors
                        throw new UserHasMultipleSalariesException(payroll.getUser().getEmail());
                    }
                    payrollRepository.save(payroll);
                });
        return BasicResponse.builder().status("OK").build();
    }

    @Transactional
    public BasicResponse updatePayrollById(Long id, PayrollRequest payroll) {
        payrollRepository.save(mapRequestToPayroll(id, payroll));
        if (payrollRepository.countByUserEmail(payroll.getEmployeeEmail()) > 1) {
            throw new UserHasMultipleSalariesException(payroll.getEmployeeEmail());
        }
        return BasicResponse.builder().status("OK").build();
    }

    private Payroll mapRequestToPayroll(PayrollRequest payrollRequest) {
        Payroll payroll = modelMapper.map(payrollRequest, Payroll.class);
        User user = (User) userDetailsService.loadUserByUsername(payrollRequest.getEmployeeEmail());
        payroll.setUser(user);
        return payroll;
    }

    private Payroll mapRequestToPayroll(Long id, PayrollRequest payrollRequest) {
        Payroll payroll = mapRequestToPayroll(payrollRequest);
        payroll.setId(id);
        return payroll;
    }
}
