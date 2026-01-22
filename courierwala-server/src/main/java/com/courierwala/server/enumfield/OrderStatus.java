package com.courierwala.server.enumfield;

public enum OrderStatus {
    CREATED,            // Order created by customer
    ASSIGNED,           // Assigned to delivery staff
    PICKED_UP,          // Package picked up
    IN_TRANSIT,         // Moving between hubs
    OUT_FOR_DELIVERY,   // Final delivery attempt
    DELIVERED,          // Successfully delivered
    FAILED,             // Delivery failed
    CANCELLED            // Cancelled by customer/admin
}
