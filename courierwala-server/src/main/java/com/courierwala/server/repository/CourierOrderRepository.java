package com.courierwala.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.courierwala.server.entities.CourierOrder;

public interface CourierOrderRepository extends JpaRepository<CourierOrder, Long>{

	@Query("""
	        SELECT o FROM CourierOrder o
	        WHERE o.destinationHub IS NOT NULL
	        AND o.orderStatus IN ('AT_SOURCE_HUB', 'IN_TRANSIT')
	    """)
	    List<CourierOrder> findHubRoutedOrders();
}
