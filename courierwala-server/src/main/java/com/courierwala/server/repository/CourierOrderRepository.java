package com.courierwala.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.CourierOrder;

public interface CourierOrderRepository extends JpaRepository<CourierOrder, Long>{

}
