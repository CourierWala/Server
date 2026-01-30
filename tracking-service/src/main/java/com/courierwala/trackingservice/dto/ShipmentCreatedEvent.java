package com.courierwala.trackingservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentCreatedEvent {

    private Long orderId;
    private String trackingNumber;
    private String fromCity;
    private String toCity;
    private String initialStatus; // CREATED
    private LocalDateTime createdAt;
}
