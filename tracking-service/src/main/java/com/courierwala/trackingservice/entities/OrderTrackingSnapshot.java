package com.courierwala.trackingservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_tracking_snapshot")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTrackingSnapshot {

    @Id
    private Long orderId;

    private String trackingNumber;
    private String fromCity;
    private String toCity;
    private String currentStatus;
    private LocalDateTime createdAt;
}

