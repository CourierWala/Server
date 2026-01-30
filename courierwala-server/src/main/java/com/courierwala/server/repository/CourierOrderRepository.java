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
	                 AND da.deliveryStatus = 'HUB_ASSIGNED')
	             OR (co.orderStatus = 'OUT_FOR_DELIVERY'
	                 AND da.deliveryStatus = 'USER_PICKED_UP')
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
	                 AND da.deliveryStatus = 'HUB_PICKED_UP')
	             OR (co.orderStatus = 'OUT_FOR_DELIVERY'
	                 AND da.deliveryStatus = 'USER_PICKED_UP')
	          )
	    """)
	List<CourierOrder> findCurrentOrdersForStaff(Long staffId);

	List<CourierOrder> findByCustomerOrderByCreatedAtDesc(User customer);
	
	
	
	@Query("""
		    SELECT o
		    FROM CourierOrder o
		    JOIN DeliveryAssignment da ON da.order = o
		    WHERE o.orderStatus = com.courierwala.server.enumfield.OrderStatus.DELIVERED
		      AND da.deliveryStaff.id = :staffId
		      AND da.deliveryStatus = com.courierwala.server.enumfield.DeliveryStatus.DELIVERED
		""")
		List<CourierOrder> findDeliveredOrdersForStaff(@Param("staffId") Long staffId);

	
	@Query("""
		    SELECT o FROM CourierOrder o
		    WHERE
		        (o.orderStatus = :created AND o.sourceHub.id = :hubId)
		        OR
		        (o.orderStatus = :atHub AND o.destinationHub.id = :hubId)
		""")
		List<CourierOrder> findDashboardOrdersForHub(
		        @Param("created") OrderStatus created,
		        @Param("atHub") OrderStatus atHub,
		        @Param("hubId") Long hubId
		);



}
