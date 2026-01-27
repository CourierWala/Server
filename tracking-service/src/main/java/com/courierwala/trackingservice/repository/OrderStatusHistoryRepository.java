package com.courierwala.trackingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.trackingservice.entities.OrderStatusHistory;

public interface OrderStatusHistoryRepository
extends JpaRepository<OrderStatusHistory, Long> {

	List<OrderStatusHistory> findByOrderIdOrderByCreatedAtAsc(Long orderId);
}

