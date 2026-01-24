package com.courierwala.server.controller;

import com.courierwala.server.customerdto.CustomerProfileDto;
import com.courierwala.server.customerdto.CustomerProfileUpdateDto;
import com.courierwala.server.customerdto.LoginDTO;
import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.entities.User;
import com.courierwala.server.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

     public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signupdto) {

        customerService.signUp(signupdto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Customer registered successfully", "success"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {

        User user = customerService.login(loginDTO);
        return ResponseEntity.ok(new ApiResponse("Login Successfully", "success"));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> profile(@PathVariable Long id) {

        CustomerProfileDto response =
                customerService.getCustomerProfile(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody CustomerProfileUpdateDto dto) {

        customerService.updateCustomerProfile(id, dto);
        return ResponseEntity.ok(
                new ApiResponse("Customer profile updated successfully", "success"));
    }
}
