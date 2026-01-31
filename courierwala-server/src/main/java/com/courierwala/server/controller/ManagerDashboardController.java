package com.courierwala.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.courierwala.server.managerdto.*;
import com.courierwala.server.security.CustomUserDetails;
import com.courierwala.server.service.ManagerDashboardService;

import java.util.List;

@RestController
@RequestMapping("/api/manager/dashboard")
@RequiredArgsConstructor
public class ManagerDashboardController {

    private final ManagerDashboardService dashboardService;

    private Long getManagerId(Authentication authentication) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();
        return user.getId();
    }

    @GetMapping("/order-status")
    public List<OrderStatusCountDto> orderStatus(Authentication authentication) {
        return dashboardService.getOrderStatusStats(
                getManagerId(authentication)
        );
    }

    @GetMapping("/dispatch-trend")
    public List<DispatchTrendDto> dispatchTrend(Authentication authentication) {
        return dashboardService.getDispatchTrend(
                getManagerId(authentication)
        );
    }

    @GetMapping("/staff-load")
    public List<StaffLoadDto> staffLoad(Authentication authentication) {
        return dashboardService.getStaffLoad(
                getManagerId(authentication)
        );
    }

    @GetMapping("/vehicle-ratio")
    public List<VehicleTypeRatioDto> vehicleRatio(Authentication authentication) {
        return dashboardService.getVehicleTypeRatio(
                getManagerId(authentication)
        );
    }
}
