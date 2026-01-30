package com.courierwala.trackingservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courierwala.trackingservice.dto.TrackingResponse;
import com.courierwala.trackingservice.entities.OrderStatusHistory;
import com.courierwala.trackingservice.entities.OrderTrackingSnapshot;
import com.courierwala.trackingservice.repository.OrderStatusHistoryRepository;
import com.courierwala.trackingservice.repository.OrderTrackingSnapshotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderTrackingService {

    private final OrderStatusHistoryRepository historyRepository;
    private final OrderTrackingSnapshotRepository snapshotRepository;

	
    public TrackingResponse getTrackingByOrderId(Long orderId) {

        // 1️⃣ Get snapshot (header info)
        OrderTrackingSnapshot snapshot = snapshotRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Tracking snapshot not found"));

        // 2️⃣ Get timeline
        List<OrderStatusHistory> timeline =
                historyRepository.findByOrderIdOrderByCreatedAtAsc(orderId);

        // 3️⃣ Build response
        return TrackingResponse.builder()
                .trackingNumber(snapshot.getTrackingNumber())
                .fromCity(snapshot.getFromCity())
                .toCity(snapshot.getToCity())
                .currentStatus(snapshot.getCurrentStatus())
                .timeline(timeline)
                .build();
    }

}
