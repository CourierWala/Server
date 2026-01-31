package com.courierwala.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.dto.SendEmailDTO;
import com.courierwala.server.repository.HubRepository;
import com.courierwala.server.service.DeliveryStatsService;
import com.courierwala.server.service.EmailService;
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
	
	public final StaffService staffservice;
	private final RestClient restClient;	
	@Value("${emailservice.url}")
    private String emailUrl;



	private final DeliveryStatsService deliveryStatsService;
	private final EmailService emailService;
	private final HubRepository hubRepository;
	

	@PatchMapping("/Current-Orders/Hub/{orderId}")
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
	
	
	@PatchMapping("/Current-Orders/customer/{orderId}")
	public ResponseEntity<?> markCustomerOrderDelivered(@PathVariable Long orderId )
	{
	    try {
	        staffservice.completeCustomerPickup(orderId);
	        return ResponseEntity.ok(new ApiResponse("Order delivered  successfully to Hub ", "SUCCESS") );

	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	
	@PatchMapping("/accepted-orders/{orderId}")
	public ResponseEntity<?> pickupAssignedOrder( @PathVariable Long orderId){
	    try {
	        return ResponseEntity.ok(staffservice.pickupAssignedOrder(orderId));

	    } catch (RuntimeException e) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}

	@PatchMapping("/dashboard/Hub/{orderid}")
	public ResponseEntity<?> assignHubOrder(@PathVariable Long orderid)
	{
	    try {	    	
	    	staffservice.assignHubOrderToStaff( orderid);
	        return ResponseEntity.ok(new ApiResponse("Hub-Order assigned successfully", "SUCCESS"));
	    } catch (RuntimeException e) {
	    	System.out.println("message  :  "+ e.getMessage());
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(e.getMessage(), "FAILED"));
	    }
	}
	
	@PatchMapping("/dashboard/customer/{orderid}")
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
	@PatchMapping("/profile")
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
	
	@GetMapping("/earnings/{staffid}/weekly")
	public ResponseEntity<?> getCompletedOrdersByStaff(@PathVariable Long staffid) {
		
		return ResponseEntity.ok(deliveryStatsService.getWeeklyDeliveredOrders(staffid));
	}
	

	
	
	@PostMapping("/applyforjob")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody StaffSignupDto signupdto){
        try {
        	   staffservice.signUp(signupdto);
        	   //get managers email from hubId
        	   String managerEmail = hubRepository.getHubManagerEmail(signupdto.getHubId());
        	   
        	   SendEmailDTO email = new SendEmailDTO(null,null);
       			email.setTo(managerEmail);
       			email.setSubject("New Job Application");
       			email.setMessage("Applicants Name: "+signupdto.getName()+"\nApplicants PhoneNO: "+signupdto.getPhone()+"\n Login To Manager Account to perform furthur action!");
       			try {
       	            restClient.post()
       	                    .uri(emailUrl)
       	                    .contentType(MediaType.APPLICATION_JSON)
       	                    .accept(MediaType.APPLICATION_JSON)
       	                    .body(email)
       	                    .retrieve()
       	                    .toBodilessEntity();
       	        } catch (RestClientException ex) {
       	            System.out.printf("Email service failed", ex);
       	        }

       			
       			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Applied for job successfully", "success"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), "Failed"));
		}
    }
		
}
