package com.courierwala.trackingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.trackingservice.entities.OrderTrackingSnapshot;

public interface OrderTrackingSnapshotRepository
extends JpaRepository<OrderTrackingSnapshot, Long> {

Optional<OrderTrackingSnapshot> findByTrackingNumber(String trackingNumber);
}

