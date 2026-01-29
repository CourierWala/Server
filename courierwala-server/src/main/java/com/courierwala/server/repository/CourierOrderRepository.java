package com.courierwala.server.repository;

import java.util.List;

import com.courierwala.server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.enumfield.OrderStatus;

public interface CourierOrderRepository extends JpaRepository<CourierOrder, Long>{

	@Query("""
	        SELECT o FROM CourierOrder o
	        WHERE o.destinationHub IS NOT NULL
	        AND o.orderStatus IN ('AT_SOURCE_HUB', 'IN_TRANSIT')
	    """)
	    List<CourierOrder> findHubRoutedOrders();
	
	List<CourierOrder> findByOrderStatusIn(List<OrderStatus> statuses);
	
	@Query("""
	        SELECT co
	        FROM CourierOrder co
	        JOIN DeliveryAssignment da ON da.order = co
	        WHERE da.deliveryStaff.id = :staffId
	          AND (
	                (co.orderStatus = 'PICKUP_ASSIGNED'
	                 AND da.deliveryStatus = 'ASSIGNED')
	             OR (co.orderStatus = 'OUT_FOR_DELIVERY'
	                 AND da.deliveryStatus = 'PICKED_UP')
	          )
	    """)
	List<CourierOrder> findAcceptedOrdersForStaff(Long staffId);

	
	@Query("""
	        SELECT co
	        FROM CourierOrder co
	        JOIN DeliveryAssignment da ON da.order = co
	        WHERE da.deliveryStaff.id = :staffId
	          AND (
	                (co.orderStatus = 'PICKED_UP'
	                 AND da.deliveryStatus = 'PICKED_UP')
	             OR (co.orderStatus = 'OUT_FOR_DELIVERY'
	                 AND da.deliveryStatus = 'PICKED_UP')
	          )
	    """)
	List<CourierOrder> findCurrentOrdersForStaff(Long staffId);

	List<CourierOrder> findByCustomerOrderByCreatedAtDesc(User customer);

}
