package com.courierwala.server.controller;

import java.util.List;

import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.admindto.ManagerUpdateDto;
import com.courierwala.server.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.courierwala.server.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/managerdetails")
    public List<ManagerDetailsDto> getManagerDetails() {
        return adminService.getManagerDetails();
    }

    @PutMapping("/hubs/{hubId}/manager")
    public ResponseEntity<ApiResponse> updateManagerDetails(
            @PathVariable Long hubId,
            @RequestBody ManagerUpdateDto dto) {

        adminService.updateManagerDetails(hubId, dto);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Manager details updated successfully",
                        "SUCCESS"
                )
        );
    }

}
