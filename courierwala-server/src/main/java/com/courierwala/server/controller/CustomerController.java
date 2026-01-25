package com.courierwala.server.controller;


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

import com.courierwala.server.customerdto.CustomerProfileDto;
import com.courierwala.server.customerdto.CustomerProfileUpdateDto;
import com.courierwala.server.customerdto.LoginDTO;
import com.courierwala.server.customerdto.ShipmentRequest;
import com.courierwala.server.customerdto.ShipmentResDto;
import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.entities.User;
import com.courierwala.server.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowCredentials = "true"
	)
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

//	@PostMapping("/signup")
//	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signupdto) {
//
//		customerService.signUp(signupdto);
//		return ResponseEntity.status(HttpStatus.CREATED)
//				.body(new ApiResponse("Customer registered successfully", "success"));
//	}
//
//	@PostMapping("/login")
//	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
//
//		User user = customerService.login(loginDTO);
//
//		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Login Successfully ", "success"));
//	}


    // ================= SIGN UP =================
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signupdto) {

        customerService.signUp(signupdto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse("Customer registered successfully", "success"));
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {

        User user = customerService.login(loginDTO);

        return ResponseEntity
                .ok(new ApiResponse("Login Successfully", "success"));
    }

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
}
