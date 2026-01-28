package com.courierwala.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.DeliveryAssignment;
import com.courierwala.server.enumfield.DeliveryStatus;
import com.courierwala.server.enumfield.OrderStatus;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {

//	 @Query("""
//		        SELECT da.order
//		        FROM DeliveryAssignment da
//		        WHERE da.deliveryStaff.id = :staffId
//		          AND (
//		               (da.order.orderStatus = com.courierwala.server.enumfield.OrderStatus.PICKUP_ASSIGNED
//		                AND da.deliveryStatus = com.courierwala.server.enumfield.DeliveryStatus.ASSIGNED)
//		            OR (da.order.orderStatus = com.courierwala.server.enumfield.OrderStatus.OUT_FOR_DELIVERY
//		                AND da.deliveryStatus = com.courierwala.server.enumfield.DeliveryStatus.PICKED_UP)
//		          )
//		    """)
//		    List<CourierOrder> findAcceptedOrdersForStaff(@Param("staffId") Long staffId);
	boolean existsByOrder(CourierOrder order);
	
}
