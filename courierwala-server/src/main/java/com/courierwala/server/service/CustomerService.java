package com.courierwala.server.service;

import com.courierwala.server.customerdto.CustomerProfileDto;
import com.courierwala.server.customerdto.CustomerProfileUpdateDto;
import com.courierwala.server.customerdto.LoginDTO;
import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.entities.User;
import jakarta.validation.Valid;

public interface CustomerService {

    void signUp(SignUpDTO signupDTO);

    User login(@Valid LoginDTO loginDTO);

    CustomerProfileDto getCustomerProfile(Long customerId);

    void updateCustomerProfile(Long customerId, CustomerProfileUpdateDto dto);

}
