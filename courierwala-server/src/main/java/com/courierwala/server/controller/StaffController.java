package com.courierwala.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffController {

	@GetMapping("/{staffid}")
	public String getAllOrdersByStaff() {
		//TODO : create a responce = list of all orders with status in_transit and staffid = staffid 
		return "orderList";
	}
	
	@PatchMapping("/order/pickup/{orderid}")
	public String pickUpOrder() {
		//TODO : upadate order status to Out_For_Delivery of order with orderid 
		return "confirmation msg: Order picked up";
	}
	
	@GetMapping("/order/{orderid}")
	public String getOrderDetails() {
		//TODO : get complete Order Details of given orderId
		return "Order Details";
	}
	
	@PatchMapping("/order/delivered/{orderid}")
	public String markAsDelivered() {
		//TODO : upadate order status to Delivered of order with orderid 
		return "confirmation msg: Order Delivered";
	}
	
	@GetMapping("/profile/{staffid}")
	public String getStaffProfile() {
		//TODO : create a responce = get staff profile 
		return "staff details";
	}
	
	@PostMapping("/profile/{staffid}")
	public String updateStaffProfile() {
		//TODO : update staff profile
		return "cnf msg";
	}
	
	@GetMapping("/earnings/{staffid}")
	public String getCompletedOrdersByStaff() {
		//TODO : create a responce = list of all orders with status in_transit and staffid = staffid 
		return "orderList";
	}
}
