package com.courierwala.server.service;

import com.courierwala.server.customerdto.CustomerProfileDto;
import com.courierwala.server.customerdto.CustomerProfileUpdateDto;
import com.courierwala.server.customerdto.ShipmentRequest;
import com.courierwala.server.customerdto.ShipmentResDto;
import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.entities.User;
import jakarta.validation.Valid;

public interface CustomerService {


    CustomerProfileDto getCustomerProfile(Long customerId);

    void updateCustomerProfile(Long customerId, CustomerProfileUpdateDto dto);

	ShipmentResDto createShipment(@Valid ShipmentRequest request);

}
