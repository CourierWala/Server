package com.courierwala.server.service;

import com.courierwala.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courierwala.server.customerdto.LoginDTO;

import com.courierwala.server.dto.ApiResponse;


import com.courierwala.server.entities.DeliveryStaffProfile;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.enumfield.VehicleType;
import com.courierwala.server.repository.HubRepository;
import com.courierwala.server.repository.StaffRepository;
import com.courierwala.server.repository.UserRepository;
import com.courierwala.server.staffdto.ChangePasswordDto;
import com.courierwala.server.staffdto.StaffSignupDto;
import com.courierwala.server.staffdto.staffProfileResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{
	
	
	public final StaffRepository staffRepo;
	public final UserRepository customerRepo;
	public final HubRepository hubRepository;

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
	            .password(dto.getPassword()) // hash later
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
	    if (!user.getPassword().equals(dto.getCurrentPassword())) {
	        throw new RuntimeException("Current password is incorrect");
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




}

