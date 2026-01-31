package com.courierwala.server.controller;

import com.courierwala.server.admindto.AdminDashboardSummaryDto;
import com.courierwala.server.admindto.EmployeesByHubDto;
import com.courierwala.server.admindto.FinanceByHubDto;
import com.courierwala.server.admindto.ParcelStatusDto;
import com.courierwala.server.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;


    @GetMapping("/finance")
    public ResponseEntity<List<FinanceByHubDto>> getFinanceByHub() {
        return ResponseEntity.ok(dashboardService.getFinanceByHub());
    }

    @GetMapping("/parcel-status")
    public ResponseEntity<ParcelStatusDto> getParcelStatus() {
        return ResponseEntity.ok(dashboardService.getParcelStatus());
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeesByHubDto>> getEmployeesByHub() {
        return ResponseEntity.ok(dashboardService.getEmployeesByHub());
    }

    @GetMapping("/summary")
    public ResponseEntity<AdminDashboardSummaryDto> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }
}
