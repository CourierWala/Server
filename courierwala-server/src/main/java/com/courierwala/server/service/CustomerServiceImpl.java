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

public class CustomerServiceImpl implements CustomerService{


    private final UserRepository customerRepo;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final CourierOrderRepository courierOrderRepository;
    private final UserRepository userRepository;


    public void signUp(SignUpDTO dto) {

        if (customerRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // hash later
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
        System.out.println(dto.getName());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        customerRepo.save(user);
    }

    @Override
    public ApiResponse createShipment(ShipmentRequest req) {

        User customer = getLoggedInUser(); // from SecurityContext

        City pickupCity = getOrCreateCity(req.getPickupCity(), req);
        City deliveryCity = getOrCreateCity(req.getDeliveryCity(), req);

        Address pickupAddress = createAddress(
                customer,
                req.getPickupAddress(),
                req.getPickupPincode(),
                pickupCity
        );

        Address deliveryAddress = createAddress(
                customer,
                req.getDeliveryAddress(),
                req.getDeliveryPincode(),
                deliveryCity
        );

        CourierOrder order = CourierOrder.builder()
                .trackingNumber(generateTrackingNumber())
                .customer(customer)
                .pickupAddress(pickupAddress)
                .deliveryAddress(deliveryAddress)
                .packageWeight(req.getWeight())
                .packageSize(PackageSize.valueOf(req.getPackageSize()))
                .deliveryType(DeliveryType.valueOf(req.getDeliveryType()))
                .pickupDate(req.getPickupDate())
                .orderStatus(OrderStatus.CREATED)
                .packageDescription(req.getDescription())
                .build();

        courierOrderRepository.save(order);

        return new ApiResponse(
                "Shipment created successfully",
                "success"
        );
    }

    /* ---------------- helper methods ---------------- */

    private City getOrCreateCity(String cityName, ShipmentRequest req) {
        return cityRepository.findByCityNameIgnoreCase(cityName)
                .orElseGet(() -> cityRepository.save(
                        City.builder().cityName(cityName)
                        .latitude(req.getDeliveryLatitude())
                        .longitude(req.getDeliveryLongitude())
                        .build()
                ));
    }

    private Address createAddress(
            User user,
            String street,
            String pincode,
            City city) {

        Address address = Address.builder()
                .user(user)
                .streetAddress(street)
                .pincode(pincode)
                .city(city)
                .isDefault(false)
                .build();

        return addressRepository.save(address);
    }

    private String generateTrackingNumber() {
        return "CW" + System.currentTimeMillis();
    }

    private User getLoggedInUser() {
        // later: SecurityContextHolder
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
