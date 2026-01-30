package com.courierwala.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.courierwala.server.entities.DeliveryAssignment;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {
	@Query("""
	        SELECT 
	            FUNCTION('DAYOFWEEK', da.updatedAt) AS dayOfWeek,
	            COUNT(da.id) AS total
	        FROM DeliveryAssignment da
	        WHERE da.deliveryStatus = 'HUB_DELIVERED' OR da.deliveryStatus = 'USER_DELIVERED'
	          AND da.deliveryStaff.id = :staffId
	          AND da.updatedAt BETWEEN :weekStart AND :weekEnd
	        GROUP BY FUNCTION('DAYOFWEEK', da.updatedAt)
	    """)
	List<DayCountProjection> countWeeklyDeliveredOrders(@Param("staffId") Long staffId,@Param("weekStart") LocalDateTime weekStart,@Param("weekEnd") LocalDateTime weekEnd);

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

	
}
