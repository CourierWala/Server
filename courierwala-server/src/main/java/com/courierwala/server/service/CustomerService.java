package com.courierwala.server.service;

import com.courierwala.server.customerdto.*;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.entities.User;
import jakarta.validation.Valid;

import java.util.List;

public interface CustomerService {


    CustomerProfileDto getCustomerProfile(Long customerId);

    void updateCustomerProfile(Long customerId, CustomerProfileUpdateDto dto);

	ShipmentResDto createShipment(@Valid ShipmentRequest request);

    List<ShipmentSummaryDto> getAllMyShipments();

}
