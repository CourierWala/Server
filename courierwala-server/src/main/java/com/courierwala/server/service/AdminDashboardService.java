package com.courierwala.server.service;

import com.courierwala.server.admindto.AdminDashboardSummaryDto;
import com.courierwala.server.admindto.EmployeesByHubDto;
import com.courierwala.server.admindto.FinanceByHubDto;
import com.courierwala.server.admindto.ParcelStatusDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final EntityManager em;

    /* ================= FINANCE ================= */


    public List<FinanceByHubDto> getFinanceByHub() {

        List<Object[]> rows = em.createNativeQuery("""
        SELECT 
            h.hub_name,
            COALESCE(SUM(p.amount), 0),
            COALESCE(SUM(p.amount) * 0.75, 0)
        FROM hub h
        LEFT JOIN courier_order o 
            ON o.source_hub_id = h.id
        LEFT JOIN payment p 
            ON p.order_id = o.order_id
            AND p.status = 'SUCCESS'
        GROUP BY h.hub_name
    """).getResultList();

        return rows.stream()
                .map(r -> new FinanceByHubDto(
                        (String) r[0],
                        (BigDecimal) r[1],
                        (BigDecimal) r[2]
                ))
                .toList();
    }









    /* ================= PARCEL STATUS ================= */
    public ParcelStatusDto getParcelStatus() {
        Long success = em.createQuery(
                "SELECT COUNT(o) FROM CourierOrder o WHERE o.orderStatus = 'DELIVERED'",
                Long.class
        ).getSingleResult();

        Long failed = em.createQuery(
                "SELECT COUNT(o) FROM CourierOrder o WHERE o.orderStatus = 'FAILED'",
                Long.class
        ).getSingleResult();

        return new ParcelStatusDto(success, failed);
    }

    /* ================= EMPLOYEES ================= */
    public List<EmployeesByHubDto> getEmployeesByHub() {
        return em.createQuery("""
            SELECT new com.courierwala.server.admindto.EmployeesByHubDto(
                h.hubName,
                SUM(CASE WHEN u.role = 'ROLE_DELIVERY_STAFF' THEN 1 ELSE 0 END),
                0,
                0
            )
            FROM Hub h
            LEFT JOIN DeliveryStaffProfile dsp ON dsp.hub.id = h.id
            LEFT JOIN User u ON dsp.user.id = u.id
            GROUP BY h.hubName
        """, EmployeesByHubDto.class).getResultList();
    }

    /* ================= SUMMARY ================= */
    public AdminDashboardSummaryDto getSummary() {

        BigDecimal revenue = em.createQuery(
                "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'SUCCESS'",
                BigDecimal.class
        ).getSingleResult();

        Long hubs = em.createQuery(
                "SELECT COUNT(h) FROM Hub h",
                Long.class
        ).getSingleResult();

        Long managers = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.role = 'ROLE_STAFF_MANAGER'",
                Long.class
        ).getSingleResult();

        BigDecimal expenses = revenue.multiply(BigDecimal.valueOf(0.7));

        return new AdminDashboardSummaryDto(
                revenue,
                expenses,
                hubs,
                managers
        );
    }
}

