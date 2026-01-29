package com.courierwala.server.service;

import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.DeliveryAssignment;
import com.courierwala.server.entities.DeliveryStaffProfile;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.AssignedBy;
import com.courierwala.server.enumfield.DeliveryStatus;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.enumfield.VehicleType; // keep ONLY if used
import com.courierwala.server.repository.CourierOrderRepository;
import com.courierwala.server.repository.DeliveryAssignmentRepository;
import com.courierwala.server.repository.HubRepository;
import com.courierwala.server.repository.StaffRepository;
import com.courierwala.server.repository.UserRepository;
import com.courierwala.server.staffdto.ChangePasswordDto;
import com.courierwala.server.staffdto.CourierOrderDto;
import com.courierwala.server.staffdto.StaffSignupDto;
import com.courierwala.server.staffdto.staffProfileResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{
	
	private final PasswordEncoder passwordEncoder;
	public final StaffRepository staffRepo;
	public final UserRepository customerRepo;
	public final HubRepository hubRepository;
	private final CourierOrderRepository orderRepository;
	private final DeliveryAssignmentRepository assignmentRepository;
	private final PasswordEncoder pass;

	@Override
	public void signUp(StaffSignupDto dto) {

	    // 1️ Check email uniqueness
	    if (customerRepo.existsByEmail(dto.getEmail())) {
	        throw new IllegalStateException("Email already registered");
	    }


	    // 2 Fetch Hub
	    Hub hub = hubRepository.findById(dto.getHubId())
	            .orElseThrow(() -> new RuntimeException("Hub not found"));


	    //3 Create User
	    User user = User.builder()
	            .name(dto.getName())
	            .email(dto.getEmail())
	            .password(passwordEncoder.encode(dto.getPassword())) // hash later
	            .phone(dto.getPhone())
	            .role(Role.ROLE_DELIVERY_STAFF)
	            .status(Status.ACTIVE)
	            .build();

	    customerRepo.save(user);

	    // 4 Create Delivery Staff Profile
	    DeliveryStaffProfile staffProfile = DeliveryStaffProfile.builder()
	            .vehicleType(dto.getVehicleType())
	            .vehicleNumber(dto.getVehicleNumber())
	            .licenseNumber(dto.getLicenseNumber())
	            .hub(hub)
	            .user(user)
	            .isVerified(false)
	            .isAvailable(false)
	            .rating(0.0)
	            .totalDeliveries(0)
	            .activeOrders(0)
	            .build();

	    staffRepo.save(staffProfile);
	}
	

	@Override
	public void login(@Valid LoginDTO loginDTO) {

	    // 1️ Find user by email
	    User user = customerRepo.findByEmail(loginDTO.getEmail())
	            .orElseThrow(() -> new IllegalStateException("Invalid email or password"));
	    
	    // 2 check for password
	    if (!user.getPassword().equals(loginDTO.getPassword())) {
	        throw new IllegalStateException("Invalid email or password");
	    }

	    // 3️ Check user status
	    if (user.getStatus() != Status.ACTIVE) {
	        throw new IllegalStateException("User account is inactive");
	    }
   
	}

	
	
	@Override
	public staffProfileResponseDTO getStaffProfile(Long staffId) {
		
		//check for staff
	    DeliveryStaffProfile staffProfile = staffRepo.findById(staffId)
	            .orElseThrow(() ->
	                    new RuntimeException("Staff profile not found with id: " + staffId)
	            );

	    //get user
	    User user = staffProfile.getUser();
	    
	    if(user.getRole() != Role.ROLE_DELIVERY_STAFF) {
	    	throw new RuntimeException("Staff profile not found with id  ..: " + staffId);
	    }
	    
	    // Split name (basic handling)
	    String firstName = user.getName();
	    String lastName = "";

	    if (user.getName() != null && user.getName().contains(" ")) {
	        String[] parts = user.getName().split(" ", 2);
	        firstName = parts[0];
	        lastName = parts[1];
	    }

	    return staffProfileResponseDTO.builder()
	            .firstName(firstName)
	            .lastName(lastName)
	            .email(user.getEmail())
	            .phone(user.getPhone())
	            .address(null) // add address mapping later if needed
	            .vehicleType(
	                staffProfile.getVehicleType() != null
	                    ? staffProfile.getVehicleType().name()
	                    : null
	            )
	            .vehicleNumber(staffProfile.getVehicleNumber())
	            .build();
	}


	@Override
	public ApiResponse updateStaffProfile(Long staffId, staffProfileResponseDTO dto) {
		
		//check for email
		DeliveryStaffProfile staffProfile = staffRepo.findById(staffId)
	            .orElseThrow(() ->
	                    new RuntimeException("Staff profile not found with id: " + staffId)
	            );
		
		User user = staffProfile.getUser();

		// Role validation
	    if (user.getRole() != Role.ROLE_DELIVERY_STAFF) {
	        throw new RuntimeException("Invalid staff profile");
	    }
	    
	    //merge first and last name
	    String fullName = dto.getFirstName();
	    if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
	        fullName = dto.getFirstName() + " " + dto.getLastName();
	    }
	    user.setName(fullName);
	    user.setEmail(dto.getEmail());
	    // Update phone
	    user.setPhone(dto.getPhone());

	    // Update vehicle details
	    if (dto.getVehicleType() != null) {
	        staffProfile.setVehicleType(
	                VehicleType.valueOf(dto.getVehicleType())
	        );
	    }
	    staffProfile.setVehicleNumber(dto.getVehicleNumber());
	    
	    // Save staffProfile changes
	    staffRepo.save(staffProfile);
	    
	 // Fetch updated profile
	    staffProfileResponseDTO updatedProfile = getStaffProfile(staffId);

	    // Return ApiResponse
	    return new ApiResponse("staff profile updated successfully", "success");
	}


	@Override
	public ApiResponse changePassword(Long staffId, @Valid ChangePasswordDto dto) {
		
		//check for email
		DeliveryStaffProfile staffProfile = staffRepo.findById(staffId)
	            .orElseThrow(() ->
	                    new RuntimeException("Staff profile not found with id: " + staffId)
	            );

	    User user = staffProfile.getUser();
	    
	    // Role validation
	    if (user.getRole() != Role.ROLE_DELIVERY_STAFF) {
	        throw new RuntimeException("Invalid staff");
	    }

	    // Validate current password
	    if (!(pass.matches(dto.getCurrentPassword(), user.getPassword())  ) ) {
	        throw new RuntimeException("Current password is incorrect "+user.getPassword()+ ""+ dto.getCurrentPassword());
	    }

	    //  Validate new and confirm password
	    if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
	        throw new RuntimeException("New password and confirm password do not match");
	    }
	    
	 // Update password 
	    user.setPassword(dto.getNewPassword());

	    // Save changes
	    customerRepo.save(user);

		return new ApiResponse("password updated successfully","success");
	}


	@Override
	public List<CourierOrderDto> getDashboardOrders() {

	    List<OrderStatus> statuses = List.of(OrderStatus.CREATED, OrderStatus.AT_DESTINATION_HUB );

	    List<CourierOrder> orders = orderRepository.findByOrderStatusIn(statuses);

	    List<CourierOrderDto> dtoList = new ArrayList<>();

	    for (CourierOrder order : orders) {

	        CourierOrderDto dto = CourierOrderDto.builder()
	                .trackingNumber(order.getTrackingNumber())
	                .Orderid(order.getId())
	                .status(order.getOrderStatus())

	                // CUSTOMER
	                .customerId(order.getCustomer().getId())
	                .customerName(order.getCustomer().getName())

	                // ADDRESSES
	                .pickupAddress(
	                        order.getPickupAddress() != null
	                                ? order.getPickupAddress().getFullAddress()
	                                : null
	                )
	                .deliveryAddress(
	                        order.getDeliveryAddress() != null
	                                ? order.getDeliveryAddress().getFullAddress()
	                                : null
	                )

	                // HUBS
	                .sourceHubId(order.getSourceHub().getId())
	                .sourceHubName(order.getSourceHub().getHubName())

	                .destinationHubId(
	                        order.getDestinationHub() != null
	                                ? order.getDestinationHub().getId()
	                                : null
	                )
	                .destinationHubName(
	                        order.getDestinationHub() != null
	                                ? order.getDestinationHub().getHubName()
	                                : null
	                )

	                // PACKAGE
	                .packageWeight(order.getPackageWeight())
	                .packageSize(order.getPackageSize())
	                .deliveryType(order.getDeliveryType())
	                .distanceKm(order.getDistanceKm())
	                .price(order.getPrice())

	                .build();

	        dtoList.add(dto);
	    }

	    return dtoList;
	}


	@Override
	public List<CourierOrderDto> getAcceptedOrders(Long staffId) {

	    List<CourierOrder> orders =
	    		orderRepository.findAcceptedOrdersForStaff(staffId);

	    List<CourierOrderDto> dtoList = new ArrayList<>();

	    for (CourierOrder order : orders) {

	        CourierOrderDto dto = CourierOrderDto.builder()
	                .trackingNumber(order.getTrackingNumber())
	                .Orderid(order.getId())
	                .status(order.getOrderStatus())
	                // CUSTOMER
	                .customerId(order.getCustomer().getId())
	                .customerName(order.getCustomer().getName())

	                // ADDRESSES
	                .pickupAddress(order.getPickupAddress().getFullAddress())
	                .deliveryAddress(order.getDeliveryAddress().getFullAddress())

	                // HUBS
	                .sourceHubId(order.getSourceHub().getId())
	                .sourceHubName(order.getSourceHub().getHubName())

	                .destinationHubId(
	                        order.getDestinationHub() != null
	                                ? order.getDestinationHub().getId()
	                                : null
	                )
	                .destinationHubName(
	                        order.getDestinationHub() != null
	                                ? order.getDestinationHub().getHubName()
	                                : null
	                )

	                // PACKAGE
	                .packageWeight(order.getPackageWeight())
	                .packageSize(order.getPackageSize())
	                .deliveryType(order.getDeliveryType())
	                .distanceKm(order.getDistanceKm())
	                .price(order.getPrice())

	                .build();

	        dtoList.add(dto);
	    }
	    return dtoList;
	}


	@Override
	public List<CourierOrderDto> getCurrentOrders(Long staffId) {
		List<CourierOrder> orders = orderRepository.findCurrentOrdersForStaff(staffId);

	    List<CourierOrderDto> dtoList = new ArrayList<>();

	    for (CourierOrder order : orders){

	        CourierOrderDto dto = CourierOrderDto.builder()
	                .trackingNumber(order.getTrackingNumber())
	                .Orderid(order.getId())
	                .status(order.getOrderStatus())
	                // CUSTOMER
	                .customerId(order.getCustomer().getId())
	                .customerName(order.getCustomer().getName())

	                // ADDRESSES
	                .pickupAddress(order.getPickupAddress().getFullAddress())
	                .deliveryAddress(order.getDeliveryAddress().getFullAddress())

	                // HUBS
	                .sourceHubId(order.getSourceHub().getId())
	                .sourceHubName(order.getSourceHub().getHubName())

	                .destinationHubId(
	                        order.getDestinationHub() != null
	                                ? order.getDestinationHub().getId()
	                                : null
	                )
	                .destinationHubName(
	                        order.getDestinationHub() != null
	                                ? order.getDestinationHub().getHubName()
	                                : null
	                )

	                // PACKAGE
	                .packageWeight(order.getPackageWeight())
	                .packageSize(order.getPackageSize())
	                .deliveryType(order.getDeliveryType())
	                .distanceKm(order.getDistanceKm())
	                .price(order.getPrice())

	                .build();

	        dtoList.add(dto);
	    }
	    return dtoList;
	}


	@Override
	public void assignOrderToStaff(Long staffId, Long orderid) {
		// TODO Auto-generated method stub
		
		
		
		// Fetch staff profile
	    DeliveryStaffProfile staff = staffRepo.findById(staffId)
	            .orElseThrow(() ->
	                    new RuntimeException("Delivery staff not found")
	            );

	    //  Fetch order
	    CourierOrder order = orderRepository.findById(orderid)
	            .orElseThrow(() ->
	                    new RuntimeException("Order not found")
	            );

	    //  Validate order status
	    if (order.getOrderStatus() != OrderStatus.CREATED) {
	        throw new RuntimeException(
	                "Order is not available for assignment"
	        );
	    }

	    //  Prevent duplicate assignment
	    if (assignmentRepository.existsByOrder(order)) {
	        throw new RuntimeException("Order already assigned");
	    }

	    //  Update order status
	    order.setOrderStatus(OrderStatus.PICKUP_ASSIGNED);
	    orderRepository.save(order);

	    //  Create delivery assignment
	    DeliveryAssignment assignment = DeliveryAssignment.builder()
	            .order(order)
	            .deliveryStaff(staff)
	            .manager(staff.getHub().getManager())
	            .assignedBy(AssignedBy.MANAGER)
	            .deliveryStatus(DeliveryStatus.ASSIGNED)
	            .build();

	    assignmentRepository.save(assignment);
		
	}


	@Override
	public void assignHubOrderToStaff(Long staffId, Long orderid) {
		
		// Fetch staff profile
	    DeliveryStaffProfile staff = staffRepo.findById(staffId)
	            .orElseThrow(() ->
	                    new RuntimeException("Delivery staff not found")
	            );

	    //  Fetch order
	    CourierOrder order = orderRepository.findById(orderid)
	            .orElseThrow(() ->
	                    new RuntimeException("Order not found")
	            );

	    //  Validate order status
	    if (order.getOrderStatus() != OrderStatus.AT_DESTINATION_HUB){
	        throw new RuntimeException(
	                "Order is not available for assignment in hub"
	        );
	    }

	    //  Prevent duplicate assignment
	    if (assignmentRepository.existsByOrder(order)) {
	        throw new RuntimeException("Order already assigned");
	    }

	    //  Update order status
	    order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
	    
	    //Increment active orders safely
	    int currentActive = staff.getActiveOrders() != null ? staff.getActiveOrders() : 0;

	    staff.setActiveOrders(currentActive+1);
	    orderRepository.save(order);

	    //  Create delivery assignment
	    DeliveryAssignment assignment = DeliveryAssignment.builder()
	            .order(order)
	            .deliveryStaff(staff)
	            .manager(staff.getHub().getManager())
	            .assignedBy(AssignedBy.MANAGER)
	            .deliveryStatus(DeliveryStatus.PICKED_UP)
	            .build();

	    assignmentRepository.save(assignment);
	    staffRepo.save(staff);
		
	}


	@Override
	public ApiResponse pickupAssignedOrder(Long staffId, Long orderId){
		
		 // Fetch Order
	    CourierOrder order = orderRepository.findById(orderId)
	            .orElseThrow(() ->
	                    new RuntimeException("Order not found")
	            );

	    //  Validate Order Status
	    if (order.getOrderStatus() != OrderStatus.PICKUP_ASSIGNED) {
	        throw new RuntimeException("Order is not in PICKUP_ASSIGNED state");
	    }

	    // 3 Fetch Assignment
	    DeliveryAssignment assignment =
	            assignmentRepository.findByOrder(order)
	                    .orElseThrow(() ->
	                            new RuntimeException("Delivery assignment not found")
	                    );

	    //  Validate Staff
	    if (!assignment.getDeliveryStaff().getId().equals(staffId)) {
	        throw new RuntimeException("Order not assigned to this staff");
	    }

	    //  Validate Assignment Status
	    if (assignment.getDeliveryStatus() != DeliveryStatus.ASSIGNED) {
	        throw new RuntimeException("Order already picked or invalid state");
	    }

	    //  Update statuses
	    order.setOrderStatus(OrderStatus.PICKED_UP);
	    assignment.setDeliveryStatus(DeliveryStatus.PICKED_UP);

	    //  Save
	    orderRepository.save(order);
	    assignmentRepository.save(assignment);

	    return new ApiResponse("Order picked up successfully", "SUCCESS");
		
	}


	@Override
	public void completeCustomerPickup(Long staffId, Long orderId) {
		
		 CourierOrder order = orderRepository.findById(orderId)
		            .orElseThrow(() -> new RuntimeException("Order not found"));

		    if (order.getOrderStatus() != OrderStatus.PICKED_UP) {
		        throw new RuntimeException("Order is not in PICKED_UP state");
		    }

		    DeliveryAssignment assignment =
		            assignmentRepository.findByOrderId(orderId)
		                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

		    if (!assignment.getDeliveryStaff().getId().equals(staffId)) {
		        throw new RuntimeException("Order not assigned to this staff");
		    }

		    // Update order status
		    order.setOrderStatus(OrderStatus.AT_SOURCE_HUB);

		    // Update assignment status 
		    assignment.setDeliveryStatus(DeliveryStatus.DELIVERED);

		    // Update staff stats
		    DeliveryStaffProfile staff = assignment.getDeliveryStaff();
		    staff.setActiveOrders(staff.getActiveOrders() - 1);
		    staff.setTotalDeliveries(staff.getTotalDeliveries() + 1);

		    orderRepository.save(order);
		    assignmentRepository.save(assignment);
		    staffRepo.save(staff);
		
	}


	@Override
	public void completeHuborderPickup(Long staffId, Long orderId) {
	
		CourierOrder order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new RuntimeException("Order not found"));

	    if (order.getOrderStatus() != OrderStatus.OUT_FOR_DELIVERY) {
	        throw new RuntimeException("Order is not in OUT_FOR_DELIVERY state");
	    }

	    DeliveryAssignment assignment =
	            assignmentRepository.findByOrderId(orderId)
	                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

	    if (!assignment.getDeliveryStaff().getId().equals(staffId)) {
	        throw new RuntimeException("Order not assigned to this staff");
	    }

	    // Update order status
	    order.setOrderStatus(OrderStatus.DELIVERED);

	    // Update assignment status 
	    assignment.setDeliveryStatus(DeliveryStatus.DELIVERED);

	    // Update staff stats
	    DeliveryStaffProfile staff = assignment.getDeliveryStaff();
	    staff.setActiveOrders(staff.getActiveOrders() - 1);
	    staff.setTotalDeliveries(staff.getTotalDeliveries() + 1);

	    orderRepository.save(order);
	    assignmentRepository.save(assignment);
	    staffRepo.save(staff);	
	}



	



	
	
	
	
	
	

	




}

