package com.courierwala.trackingservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.courierwala.trackingservice.config.RabbitMQConfig;
import com.courierwala.trackingservice.dto.OrderStatusEvent;
import com.courierwala.trackingservice.dto.ShipmentCreatedEvent;
import com.courierwala.trackingservice.entities.OrderStatus;
import com.courierwala.trackingservice.entities.OrderStatusHistory;
import com.courierwala.trackingservice.entities.OrderTrackingSnapshot;
import com.courierwala.trackingservice.repository.OrderStatusHistoryRepository;
import com.courierwala.trackingservice.repository.OrderTrackingSnapshotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStatusEventListener {

    private final OrderStatusHistoryRepository historyRepository;
    private final OrderTrackingSnapshotRepository snapshotRepository;

    // -------- ORDER STATUS EVENT (TIMELINE) --------

    
    @RabbitListener(queues = RabbitMQConfig.TRACKING_QUEUE)
    public void consumeOrderStatusEvent(OrderStatusEvent event) {

        // 1️⃣ Save timeline
        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(event.getOrderId())
                .status(OrderStatus.valueOf(event.getStatus()))
                .remarks(event.getRemarks())
                .updatedByUserId(event.getUpdatedBy())
                .build();

        historyRepository.save(history);

        // 2️⃣ Update snapshot current status (if exists)
        snapshotRepository.findById(event.getOrderId())
                .ifPresent(snapshot -> {
                    snapshot.setCurrentStatus(event.getStatus());
                    snapshotRepository.save(snapshot);
                });
    }

    // -------- SHIPMENT CREATED EVENT (SNAPSHOT) --------

    @RabbitListener(queues = RabbitMQConfig.SHIPMENT_CREATED_QUEUE)
    public void consumeShipmentCreated(ShipmentCreatedEvent event) {

        // Prevent duplicate snapshot creation
        if (snapshotRepository.existsById(event.getOrderId())) {
            return;
        }

        OrderTrackingSnapshot snapshot = OrderTrackingSnapshot.builder()
                .orderId(event.getOrderId())
                .trackingNumber(event.getTrackingNumber())
                .fromCity(event.getFromCity())
                .toCity(event.getToCity())
                .currentStatus(event.getInitialStatus())
                .createdAt(event.getCreatedAt())
                .build();

        snapshotRepository.save(snapshot);
    }
}
