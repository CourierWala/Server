package com.courierwala.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusEvent {

    private Long orderId;
    private String status;
    private Long updatedBy;
    private String remarks;
    private LocalDateTime timestamp;
}
