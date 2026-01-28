package com.courierwala.server.customerdto;

import java.time.LocalDate;

import com.courierwala.server.enumfield.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShipmentSummaryDto {

    private Long orderId;
    private String trackingNumber;

    private String pickupCity;
    private String deliveryCity;

    private OrderStatus orderStatus;
    private Double price;

    private LocalDate pickupDate;
}