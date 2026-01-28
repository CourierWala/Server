package com.courierwala.trackingservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.courierwala.trackingservice.entities.OrderStatusHistory;
import com.courierwala.trackingservice.repository.OrderStatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderTrackingService {
	
	private final OrderStatusHistoryRepository repository;
	
	public List<OrderStatusHistory> getOrderTimeline(Long orderId) {
	    return repository.findByOrderIdOrderByCreatedAtAsc(orderId);
	}

}
