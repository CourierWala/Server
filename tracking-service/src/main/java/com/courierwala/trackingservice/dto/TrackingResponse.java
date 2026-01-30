package com.courierwala.trackingservice.dto;

import java.util.List;

import com.courierwala.trackingservice.entities.OrderStatusHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingResponse {

    private String trackingNumber;
    private String fromCity;
    private String toCity;
    private String currentStatus;
    private List<OrderStatusHistory> timeline;
}

