package com.courierwala.server.controller;

import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.GetStaffDto;
import com.courierwala.server.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/acceptStaff/{applicationId}")
    public ResponseEntity<ApiResponse> acceptDeliveryStaff(
            @PathVariable Long applicationId) {

        managerService.acceptDeliveryStaff(applicationId);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Delivery staff hired successfully!!!",
                        "SUCCESS"
                )
        );
    }

    @GetMapping("/applications")
    public List<GetStaffDto> getAllJobApplications() {
        return managerService.getAllStaff(false);
    }

    @GetMapping("/current-staff")
    public List<GetStaffDto> getAllCurrentStaff() {
        return managerService.getAllStaff(true);
    }

    @GetMapping("/rejectStaff/{rejectApplicationId}")
    public ResponseEntity<ApiResponse> rejectApplication(
            @PathVariable Long rejectApplicationId) {

        managerService.rejectApplication(rejectApplicationId);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Job application rejected!!!",
                        "SUCCESS"
                )
        );
    }
}
