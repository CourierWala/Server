package com.courierwala.server.controller;

import java.util.List;

import com.courierwala.server.admindto.ManagerDetailsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
