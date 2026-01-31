package com.courierwala.trackingservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.trackingservice.dto.TrackingResponse;
import com.courierwala.trackingservice.service.OrderTrackingService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(
	    origins = "http://localhost:5173/",
	    allowCredentials = "true"
	)

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracking")
public class OrderTrackingController {

	private final OrderTrackingService orderTrackingService;
	
	@GetMapping("/{trackingNumber}")
	public ResponseEntity<?> getTimeline(@PathVariable String trackingNumber) {
     try {
 		TrackingResponse trackingResponce = orderTrackingService.getTrackingByOrderId(trackingNumber);
 		return ResponseEntity.ok(trackingResponce);
	} catch (Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Order Found with this Tracking Number !!");
	}
		
	}
}
