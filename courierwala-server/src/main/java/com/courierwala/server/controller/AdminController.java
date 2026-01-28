package com.courierwala.server.controller;

import java.util.List;

import com.courierwala.server.admindto.AdminProfileUpdateDto;
import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.admindto.ManagerUpdateDto;
import com.courierwala.server.admindto.PriceChangeDto;
import com.courierwala.server.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.courierwala.server.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/profile/{adminId}")
    public ResponseEntity<ApiResponse> updateAdminProfile(
            @PathVariable Long adminId,
            @RequestBody AdminProfileUpdateDto dto) {

        adminService.updateAdminProfile(adminId, dto);

        return ResponseEntity.ok(
                new ApiResponse("Profile updated successfully", "SUCCESS")
        );
    }




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

    @PostMapping("/pricechange")
    public ResponseEntity<ApiResponse> changePrice(
            @RequestBody PriceChangeDto dto) {

        adminService.changePrice(dto);

        return ResponseEntity.ok(
                new ApiResponse("Price updated successfully", "SUCCESS")
        );
    }

    @GetMapping("/priceconfig")
    public ResponseEntity<PriceChangeDto> getPriceConfig() {
        return ResponseEntity.ok(
                adminService.getPriceConfig()
        );
    }


}
