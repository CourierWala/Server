package com.courierwala.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.entities.DeliveryStaffProfile;
import com.courierwala.server.entities.User;
import com.courierwala.server.service.CustomerService;
import com.courierwala.server.service.StaffService;
import com.courierwala.server.staffdto.ChangePasswordDto;
import com.courierwala.server.staffdto.CourierOrderDto;
import com.courierwala.server.staffdto.StaffSignupDto;
import com.courierwala.server.staffdto.staffProfileResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowCredentials = "true"
	)
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {  
	
	@Autowired
	public final StaffService staffservice;
	@PutMapping("/Current-Orders/Hub/{orderId}")
	public ResponseEntity<?> markHubOrderDelivered(@PathVariable Long orderId )
	{
	    try {
	        staffservice.completeHuborderPickup(orderId);
	        return ResponseEntity.ok( new ApiResponse("Order delivered successfully", "SUCCESS"));

	    } catch (RuntimeException e){
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	
	@PutMapping("/Current-Orders/customer/{orderId}")
	public ResponseEntity<?> markCustomerOrderDelivered(@PathVariable Long orderId )
	{
	    try {
	        staffservice.completeCustomerPickup(orderId);
	        return ResponseEntity.ok(new ApiResponse("Order delivered  successfully to Hub ", "SUCCESS") );

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	
	@PutMapping("/accepted-orders/{orderId}")
	public ResponseEntity<?> pickupAssignedOrder( @PathVariable Long orderId){
	    try {
	        return ResponseEntity.ok(staffservice.pickupAssignedOrder(orderId));

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	@PutMapping("/dashboard/Hub/{orderid}")
	public ResponseEntity<?> assignHubOrder(@PathVariable Long orderid)
	{
	    try {	    	
	    	staffservice.assignHubOrderToStaff( orderid);

	        return ResponseEntity.ok(new ApiResponse("Hub-Order assigned successfully", "SUCCESS"));
	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	@PutMapping("/dashboard/customer/{orderid}")
	public ResponseEntity<?> assignOrder(@PathVariable Long orderid)
	{
	    try {
	    	staffservice.assignOrderToStaff(orderid);
	    	
	        return ResponseEntity.ok(new ApiResponse("Customer-Order assigned successfully", "SUCCESS"));

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	@GetMapping("/dashboard")
	public ResponseEntity<?> getAvailableOrders(){
	    try {
	        List<CourierOrderDto> orders = staffservice.getDashboardOrders();
	        //System.out.println("call");
	        return ResponseEntity.ok(orders);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	@GetMapping("/accepted-orders")
	public ResponseEntity<?> getAcceptedOrders(){
	    try {
	       
	        List<CourierOrderDto> orders =
	        		staffservice.getAcceptedOrders();

	        return ResponseEntity.ok(orders);

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}	
	
	@GetMapping("/current-orders")
	public ResponseEntity<?> getCurrentOrders( ) {
	    try {
	        
	        List<CourierOrderDto> orders =
	        		staffservice.getCurrentOrders();

	        return ResponseEntity.ok(orders);

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	@GetMapping("/delivered-orders")
	public ResponseEntity<?> getDeliveredOrders() {
	    try {
	        List<CourierOrderDto> orders =
	                staffservice.getDeliveredOrdersForStaff();

	        return ResponseEntity.ok(orders);

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}


	@GetMapping("/profile")
	public ResponseEntity<?> getStaffProfile() {
	    try {
	        staffProfileResponseDTO response =
	                staffservice.getStaffProfile();

	        return ResponseEntity.status(HttpStatus.OK).body(response);
	               
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse(e.getMessage(), "Failed"));
	    }
	}

	//Tested
	// Returns the Conformation msg if updated successfully
	@PostMapping("/profile")
	public ResponseEntity<ApiResponse> updateStaffProfile( @RequestBody staffProfileResponseDTO dto) {

	    try {
	        
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(staffservice.updateStaffProfile(dto));

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse(e.getMessage(), "Failed"));
	    }
	}
	
	
	@PostMapping("/profile/changepassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto dto)
	{
		try {
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(staffservice.changePassword( dto));

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(),"failed"));
	    }
	}
	
	
	@PostMapping("/applyforjob")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody StaffSignupDto signupdto){
        try {
        	   staffservice.signUp(signupdto);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("signup sucessfully", "success"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), "Failed"));
		}
    }
	


	
}
