package com.courierwala.server.controller;

import com.courierwala.server.customerdto.CustomerProfileDto;
import com.courierwala.server.customerdto.CustomerProfileUpdateDto;
import com.courierwala.server.customerdto.LoginDTO;
import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.entities.User;
import com.courierwala.server.service.CustomerService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
 public class CustomerController {


    @Autowired
    private final CustomerService customerService   ;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody SignUpDTO signupdto) {

        customerService.signUp(signupdto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Customer registered successfully","success"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDTO loginDTO) {

        User user = customerService.login(loginDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Login Successfully ","success"));
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

        try{
            customerService.updateCustomerProfile(id, dto);
            return ResponseEntity.ok(new ApiResponse("Customer profile updated successfully","success"));

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse("Customer profile not  updated  ","error"));


        }

        //Update Password
        //Shipping History-tracking number,source destination,parcel type ,status,
        //Track Package -i/p-track no o/p-- ParcelDto
        //Recent Shipment

    }




}
