package com.courierwala.server.controller;

import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.GetStaffDto;
import com.courierwala.server.dto.SendEmailDTO;
import com.courierwala.server.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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
    private final RestClient restClient;
    @Value("${emailservice.url}")
    private String emailUrl;

    @GetMapping("/acceptStaff/{applicationId}/{applicantEmail}")
    public ResponseEntity<ApiResponse> acceptDeliveryStaff(
            @PathVariable Long applicationId, @PathVariable String applicantEmail) {

        managerService.acceptDeliveryStaff(applicationId);

        SendEmailDTO emailBody = new SendEmailDTO("Congratulations!!! Job application accepted", "You can now login in your account by \nEmail: " + applicantEmail + "\nPassword: Pass@123");
        emailBody.setTo(applicantEmail);
        try {
            restClient.post()
                    .uri(emailUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(emailBody)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            System.out.printf("Email service failed", ex);
        }

        return ResponseEntity.ok(
                new ApiResponse(
                        "Delivery staff hired successfully!!!",
                        "SUCCESS"
                )
                
        );
    }

    @GetMapping("/applications/{managerId}")
    public List<GetStaffDto> getAllJobApplications(@PathVariable Long managerId) {
        return managerService.getAllStaff(false, managerId);
    }

    @GetMapping("/current-staff/{managerId}")
    public List<GetStaffDto> getAllCurrentStaff(@PathVariable Long managerId) {
        return managerService.getAllStaff(true, managerId);
    }

    @GetMapping("/rejectStaff/{rejectApplicationId}/{applicantEmail}")
    public ResponseEntity<ApiResponse> rejectApplication(
            @PathVariable Long rejectApplicationId, @PathVariable String applicantEmail) {

        managerService.rejectApplication(rejectApplicationId);


        SendEmailDTO emailBody = new SendEmailDTO("Job application rejected", "You can try again later");
        emailBody.setTo(applicantEmail);
        try {
            restClient.post()
                    .uri(emailUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(emailBody)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            System.out.printf("Email service failed", ex);
        }
        return ResponseEntity.ok(
                new ApiResponse(
                        "Job application rejected!!!",
                        "SUCCESS"
                )
        );
    }
}
