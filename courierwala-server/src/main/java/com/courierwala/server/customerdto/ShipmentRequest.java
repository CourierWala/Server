package com.courierwala.server.customerdto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {

    @NotBlank
    private String pickupAddress;

    @NotBlank
    private String pickupCity;

    @Pattern(regexp = "\\d{6}", message = "Pickup pincode must be 6 digits")
    private String pickupPincode;

    @NotBlank
    private String deliveryAddress;

    @NotBlank
    private String deliveryCity;

    @Pattern(regexp = "\\d{6}", message = "Delivery pincode must be 6 digits")
    private String deliveryPincode;

    @NotNull
    @FutureOrPresent
    private LocalDate pickupDate;

    @NotNull
    private Double pickupLatitude;

    @NotNull
    private Double pickupLongitude;

    @NotNull
    private Double deliveryLatitude;

    @NotNull
    private Double deliveryLongitude;

    @Positive
    private Double weight;

    private String packageSize;
    private String deliveryType;
    private String description;

}
