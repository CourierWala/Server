package com.courierwala.server.repository;

import com.courierwala.server.entities.DeliveryStaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryStaffProfileRepository extends JpaRepository<DeliveryStaffProfile, Long> {
    List<DeliveryStaffProfile> findAllByIsVerified(Boolean isVerified);
}
