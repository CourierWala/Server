package com.courierwala.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.server.customerdto.LoginDTO;
import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.entities.DeliveryStaffProfile;
import com.courierwala.server.entities.User;
import com.courierwala.server.service.CustomerService;
import com.courierwala.server.service.StaffService;
import com.courierwala.server.staffdto.ChangePasswordDto;
import com.courierwala.server.staffdto.StaffSignupDto;
import com.courierwala.server.staffdto.staffProfileResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
	
	@Autowired
	public final StaffService staffservice;

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
	
	@GetMapping("/profile/{staffId}")
	public ResponseEntity<?> getStaffProfile(@PathVariable Long staffId) {
	    try {
	        staffProfileResponseDTO response =
	                staffservice.getStaffProfile(staffId);

	        return ResponseEntity.status(HttpStatus.OK).body(response);
	               
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse(e.getMessage(), "Failed"));
	    }
	}

	
	@PostMapping("/profile/{staffId}")
	public ResponseEntity<ApiResponse> updateStaffProfile( @PathVariable Long staffId, @RequestBody staffProfileResponseDTO dto) {

	    try {
	        
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(staffservice.updateStaffProfile(staffId, dto));

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse(e.getMessage(), "Failed"));
	    }
	}
	
	
	@PostMapping("/profile/{staffId}/setpassword")
	public ResponseEntity<?> changePassword( @PathVariable Long staffId, @Valid @RequestBody ChangePasswordDto dto)
	{
	
		try {
	       
	                

	        return ResponseEntity.status(HttpStatus.OK)
	                .body(staffservice.changePassword(staffId, dto));

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(),"failed"));
	    }
	}
	
	@GetMapping("/earnings/{staffid}")
	public String getCompletedOrdersByStaff() {
		//TODO : create a responce = list of all orders with status in_transit and staffid = staffid 
		return "orderList";
	}
	

	
	
	@PostMapping("/signup")
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
	
	@PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDTO loginDTO) {
		
		try {
			staffservice.login(loginDTO);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("login sucessfully", "success"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(e.getMessage(), "Failed"));
		}

         

        
    }
	
}
