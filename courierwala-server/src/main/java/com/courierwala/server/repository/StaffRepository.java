package com.courierwala.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.DeliveryStaffProfile;

public interface StaffRepository extends JpaRepository<DeliveryStaffProfile, Long>{
	
	 Optional<DeliveryStaffProfile> findByUser_Id(Long userId);
}
