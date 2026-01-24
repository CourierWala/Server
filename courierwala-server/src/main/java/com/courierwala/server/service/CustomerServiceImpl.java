package com.courierwala.server.service;

import com.courierwala.server.customerdto.*;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.entities.Address;
import com.courierwala.server.entities.City;
import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.DeliveryType;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.enumfield.PackageSize;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.repository.AddressRepository;
import com.courierwala.server.repository.CityRepository;
import com.courierwala.server.repository.CourierOrderRepository;
import com.courierwala.server.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;

    @Override
    public void signUp(SignUpDTO dto) {

        if (customerRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .role(Role.ROLE_CUSTOMER)
                .status(Status.ACTIVE)
                .build();

        customerRepo.save(user);
    }

    @Override
    public User login(LoginDTO dto) {

        User user = customerRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("User account is not active");
        }

        return user;
    }

    @Override
    public CustomerProfileDto getCustomerProfile(Long customerId) {

        User user = customerRepo.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

//        List<AddressResponse> addressResponses =
//                user.getAddresses().stream()
//                        .map(address -> AddressResponse.builder()
//                                .addressId(address.getId())
//                                .addressLine(address.getAddressLine())
//                                .pincode(address.getPincode())
//                                .cityName(address.getCity().getCityName())
//                                .isDefault(address.getIsDefault())
//                                .build()
//                        ).toList();
//
//        return CustomerProfileDto.builder()
//                .name(user.getName())
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .addresses(addressResponses)
//                .build();
        
        return null;
    }

    @Override
    public void updateCustomerProfile(Long customerId, CustomerProfileUpdateDto dto) {

        User user = customerRepo.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        customerRepo.save(user);
    }
}
