package com.courierwala.server.service;

import com.courierwala.server.customerdto.*;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;

    // ================= SIGN UP =================
    @Override
    public void signUp(SignUpDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // (later encrypt)
                .phone(dto.getPhone())
                .role(Role.ROLE_CUSTOMER)
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public User login(LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("User account is not active");
        }

        return user;
    }

    // ================= VIEW PROFILE =================
    @Override
    public CustomerProfileDto getCustomerProfile(Long customerId) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

        return CustomerProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    // ================= UPDATE PROFILE =================
    @Override
    public void updateCustomerProfile(Long customerId, CustomerProfileUpdateDto dto) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        userRepository.save(user);
    }

    @Override
     public ApiResponse createShipment(@Valid ShipmentRequest request) {

        // later you will save CourierOrder, Address, etc.
        return new ApiResponse("Shipment created successfully", "success");
    }

}
