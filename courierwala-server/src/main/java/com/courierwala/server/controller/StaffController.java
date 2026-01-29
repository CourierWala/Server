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
//
//	@GetMapping("/{staffid}")
//	public String getAllOrdersByStaff() {
//		//TODO : create a responce = list of all orders with status in_transit and staffid = staffid 
//		return "orderList";
//	}
	
	@PatchMapping("/order/pickup/{orderid}")
	public String pickUpOrder() {
		//TODO : upadate order status to Out_For_Delivery of order with orderid 
		return "confirmation msg: Order picked up";
	}
	
	
	
	@PutMapping("/Current-Orders/Hub/{orderId}")
	public ResponseEntity<?> markHubOrderDelivered(/*HttpSession session,*/@PathVariable Long orderId )
	{
	    try {
	        Long staffId = (long)1; /*session.getAttribute("staffId");*/
//	        Long orderId = body.get("orderId");

	        staffservice.completeHuborderPickup(staffId, orderId);

	        return ResponseEntity.ok( new ApiResponse("Order delivered successfully", "SUCCESS"));

	    } catch (RuntimeException e){
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	
	@PutMapping("/Current-Orders/customer/{orderId}")
	public ResponseEntity<?> markCustomerOrderDelivered(/*HttpSession session,*/@PathVariable Long orderId )
	{
	    try {
	        Long staffId = (long)1; /*session.getAttribute("staffId");*/
//	        Long orderId = body.get("orderId");
	        staffservice.completeCustomerPickup(staffId, orderId);

	        return ResponseEntity.ok(new ApiResponse("Order delivered successfully", "SUCCESS") );

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	
	@PutMapping("/accepted-orders/{orderId}")
	public ResponseEntity<?> pickupAssignedOrder( @PathVariable Long orderId /*, HttpSession session*/
	) {
	    try {
	        
	        Long staffId = (long) 1 /*session.getAttribute("staffId");*/;
	        
//	        if (staffId == null) staffId = 1L;

	        ApiResponse response = staffservice.pickupAssignedOrder(staffId, orderId);

	        return ResponseEntity.ok(response);

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	
	
	@PutMapping("/dashboard/Hub/{orderid}")
	public ResponseEntity<?> assignHubOrder(@PathVariable Long orderid /*HttpSession session*/
	) {
	    try {
//	        Long staffId = (Long) session.getAttribute("staffId");
//	        if (staffId == null) {
//	            throw new RuntimeException("Staff not logged in");
//	        }
	        //Long orderId = body.get("orderId");

	    	Long staffId = (long)1;
	    	
	    	staffservice.assignHubOrderToStaff(staffId, orderid);

	        return ResponseEntity.ok(
	                new ApiResponse("Hub-Order assigned successfully", "SUCCESS")
	        );

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	@PutMapping("/dashboard/customer/{orderid}")
	public ResponseEntity<?> assignOrder(@PathVariable Long orderid /*HttpSession session*/
	) {
	    try {
//	        Long staffId = (Long) session.getAttribute("staffId");
//	        if (staffId == null) {
//	            throw new RuntimeException("Staff not logged in");
//	        }
	    	

	        //Long orderId = body.get("orderId");

	    	Long staffId = (long)1;
	    	
	    	staffservice.assignOrderToStaff(staffId, orderid);

	        return ResponseEntity.ok(
	                new ApiResponse("Customer-Order assigned successfully", "SUCCESS")
	        );

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	
	@PatchMapping("/order/delivered/{orderid}")
	public String markAsDelivered() {
		//TODO : upadate order status to Delivered of order with orderid 
		return "confirmation msg: Order Delivered";
	}
	
	//Tested
	// Returns the profile of Delivery-Staff
	
	@GetMapping("/dashboard")
	public ResponseEntity<?> getAvailableOrders() {
	    try {
	        List<CourierOrderDto> orders = staffservice.getDashboardOrders();
	        return ResponseEntity.ok(orders);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	@GetMapping("/accepted-orders")
	public ResponseEntity<?> getAcceptedOrders(/*HttpSession session*/) {
	    try {
	        //Long staffId = (Long) session.getAttribute("staffId");
	    	Long staffId =  (long) 1;
	        List<CourierOrderDto> orders =
	        		staffservice.getAcceptedOrders(staffId);

	        return ResponseEntity.ok(orders);

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}	
	
	@GetMapping("/current-orders")
	public ResponseEntity<?> getCurrentOrders(/*HttpSession session*/) {
	    try {
	        //Long staffId = (Long) session.getAttribute("staffId");
	    	Long staffId =  (long) 1;
	        List<CourierOrderDto> orders =
	        		staffservice.getCurrentOrders(staffId);

	        return ResponseEntity.ok(orders);

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
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

	//Tested
	// Returns the Conformation msg if updated successfully
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
