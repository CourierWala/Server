package com.courierwala.server.service;

import java.util.List;

import com.courierwala.server.managerdto.*;

public interface ManagerDashboardService {

    List<OrderStatusCountDto> getOrderStatusStats(Long managerId);

    List<DispatchTrendDto> getDispatchTrend(Long managerId);

    List<StaffLoadDto> getStaffLoad(Long managerId);

    List<VehicleTypeRatioDto> getVehicleTypeRatio(Long managerId);
}
