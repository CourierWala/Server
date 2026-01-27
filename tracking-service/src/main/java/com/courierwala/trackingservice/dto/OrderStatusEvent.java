package com.courierwala.trackingservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class OrderStatusEvent {

    private Long orderId;
    private String status;
    private Long updatedBy;
    private String remarks;
    private LocalDateTime timestamp;
}
