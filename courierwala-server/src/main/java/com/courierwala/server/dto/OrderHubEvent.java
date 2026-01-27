package com.courierwala.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHubEvent {

    private Long orderId;
    private Long hubId;
    private String eventType; // ARRIVED or DEPARTED
    private LocalDateTime timestamp;
    private String remarks;
}
