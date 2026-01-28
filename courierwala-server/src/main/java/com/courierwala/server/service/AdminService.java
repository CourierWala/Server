package com.courierwala.server.service;

import com.courierwala.server.admindto.AddManagerDto;
import com.courierwala.server.admindto.AdminProfileUpdateDto;
import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.admindto.ManagerUpdateDto;

import java.util.List;

public interface AdminService {
    List<ManagerDetailsDto> getManagerDetails();

    void updateManagerDetails(Long managerId, ManagerUpdateDto dto);

    void updateAdminProfile(Long adminId, AdminProfileUpdateDto dto);

    void addManager(AddManagerDto manager);
}
