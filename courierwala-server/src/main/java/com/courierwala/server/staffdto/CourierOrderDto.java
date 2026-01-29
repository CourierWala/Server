package com.courierwala.server.staffdto;
import java.math.BigDecimal;

import com.courierwala.server.entities.Hub;
import com.courierwala.server.enumfield.DeliveryType;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.enumfield.PackageSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierOrderDto {

    private String trackingNumber;
    private Long Orderid;
    // CUSTOMER
    private Long customerId;
    private String customerName;
    private OrderStatus status;

    // ADDRESSES
    private String pickupAddress;
    private String deliveryAddress;

    // HUBS
    private Long sourceHubId;
    private String sourceHubName;

    private Long destinationHubId;
    private String destinationHubName;

    private Double packageWeight;
    private PackageSize packageSize;
    private DeliveryType deliveryType;
    private Double distanceKm;
    private BigDecimal price;
}
