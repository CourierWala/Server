package com.courierwala.trackingservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.trackingservice.dto.TrackingResponse;
import com.courierwala.trackingservice.entities.OrderStatusHistory;
import com.courierwala.trackingservice.service.OrderTrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracking")
public class OrderTrackingController {

	private final OrderTrackingService orderTrackingService;
	
	@GetMapping("/{orderId}")
	public ResponseEntity<?> getTimeline(@PathVariable Long orderId) {
     try {
 		TrackingResponse trackingResponce = orderTrackingService.getTrackingByOrderId(orderId);
 		return ResponseEntity.ok(trackingResponce);
	} catch (Exception e) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Order Found with this Tracking Number !!");
	}
		
	}
}
