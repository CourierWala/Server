package com.courierwala.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.enumfield.HubOrderStatus;
import com.courierwala.server.managerdto.*;

public interface ManagerDashboardRepository
        extends JpaRepository<CourierOrder, Long> {

    // ===== ORDER STATUS BAR CHART =====
    @Query("""
        SELECT new com.courierwala.server.managerdto.OrderStatusCountDto(
            o.orderStatus,
            COUNT(o)
        )
        FROM CourierOrder o
        WHERE o.sourceHub.manager.id = :managerId
        GROUP BY o.orderStatus
    """)
    List<OrderStatusCountDto> findOrderStatusCountsByManager(
            @Param("managerId") Long managerId
    );

    // ===== DISPATCH TREND (LINE CHART) =====
    @Query("""
    SELECT new com.courierwala.server.managerdto.DispatchTrendDto(
        t.departureTime,
        COUNT(t)
    )
    FROM OrderHubTracking t
    WHERE t.hub.manager.id = :managerId
      AND t.status = :status
      AND t.departureTime >= :fromDate
    GROUP BY t.departureTime
    ORDER BY t.departureTime
""")
    List<DispatchTrendDto> findDispatchTrendByManager(
            @Param("managerId") Long managerId,
            @Param("status") HubOrderStatus status,
            @Param("fromDate") LocalDateTime fromDate
    );


    // ===== DELIVERY STAFF LOAD =====
    @Query("""
        SELECT new com.courierwala.server.managerdto.StaffLoadDto(
            d.user.name,
            d.activeOrders
        )
        FROM DeliveryStaffProfile d
        WHERE d.hub.manager.id = :managerId
    """)
    List<StaffLoadDto> findStaffLoadByManager(
            @Param("managerId") Long managerId
    );

    // ===== VEHICLE RATIO PIE =====
    @Query("""
        SELECT new com.courierwala.server.managerdto.VehicleTypeRatioDto(
            d.vehicleType,
            COUNT(d)
        )
        FROM DeliveryStaffProfile d
        WHERE d.hub.manager.id = :managerId
        GROUP BY d.vehicleType
    """)
    List<VehicleTypeRatioDto> findVehicleTypeRatioByManager(
            @Param("managerId") Long managerId
    );
}
