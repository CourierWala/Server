package com.courierwala.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courierwala.server.enumfield.HubOrderStatus;
import com.courierwala.server.managerdto.*;
import com.courierwala.server.repository.ManagerDashboardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerDashboardServiceImpl implements ManagerDashboardService {

    private final ManagerDashboardRepository dashboardRepository;

    @Override
    public List<OrderStatusCountDto> getOrderStatusStats(Long managerId) {
        return dashboardRepository.findOrderStatusCountsByManager(managerId);
    }

    @Override
    public List<DispatchTrendDto> getDispatchTrend(Long managerId) {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);

        return dashboardRepository.findDispatchTrendByManager(
                managerId,
                HubOrderStatus.DISPATCHED_FROM_HUB,
                fromDate
        );
    }

    @Override
    public List<StaffLoadDto> getStaffLoad(Long managerId) {
        return dashboardRepository.findStaffLoadByManager(managerId);
    }

    @Override
    public List<VehicleTypeRatioDto> getVehicleTypeRatio(Long managerId) {
        return dashboardRepository.findVehicleTypeRatioByManager(managerId);
    }
}
