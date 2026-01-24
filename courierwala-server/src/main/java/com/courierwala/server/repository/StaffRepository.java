package com.courierwala.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.DeliveryStaffProfile;

public interface StaffRepository extends JpaRepository<DeliveryStaffProfile, Long>{

}
