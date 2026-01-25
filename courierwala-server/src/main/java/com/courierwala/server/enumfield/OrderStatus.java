package com.courierwala.server.enumfield;

public enum OrderStatus {
	CREATED,              // Order created, parcel with customer
	PICKUP_ASSIGNED,     // Delivery staff assigned
	PICKED_UP,           // Parcel picked from customer
	AT_SOURCE_HUB,       // Parcel reached source hub
	IN_TRANSIT,          // Moving hub to hub
	AT_DESTINATION_HUB,  // Reached destination hub
	OUT_FOR_DELIVERY,    // Last mile
	DELIVERED

}
