package com.courierwala.trackingservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.trackingservice.entities.OrderStatusHistory;
import com.courierwala.trackingservice.service.OrderTrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracking")
public class OrderTrackingController {

	private final OrderTrackingService orderTrackingService;
	
	@GetMapping("/{orderId}")
	public List<OrderStatusHistory> getTimeline(@PathVariable Long orderId) {
	    return orderTrackingService.getOrderTimeline(orderId);
	}
}
