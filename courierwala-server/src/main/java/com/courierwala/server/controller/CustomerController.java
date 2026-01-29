package com.courierwala.server.controller;


import com.courierwala.server.customerdto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.entities.User;
import com.courierwala.server.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowCredentials = "true"
	)
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;


    // ================= VIEW PROFILE =================
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> profile(@PathVariable Long id) {

        CustomerProfileDto response = customerService.getCustomerProfile(id);

        return ResponseEntity.ok(response);
    }


    // ================= UPDATE PROFILE =================
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody CustomerProfileUpdateDto dto) {

        customerService.updateCustomerProfile(id, dto);

        return ResponseEntity.ok(
                new ApiResponse("Customer profile updated successfully", "success"));
    }

  
  	@PostMapping("/shipments")
	public ResponseEntity<?> createShipment(@Valid @RequestBody ShipmentRequest request) {

		System.out.println("in create shipment !!");
		ShipmentResDto shipmentResponce = customerService.createShipment(request);
		System.out.println("shipemt res : " + shipmentResponce);

		return ResponseEntity.status(HttpStatus.CREATED).body(shipmentResponce);
	}


    @GetMapping("/shipments")
    public ResponseEntity<List<ShipmentSummaryDto>> getAllMyShipments() {
        return ResponseEntity.ok(
                customerService.getAllMyShipments()
        );
    }
}
