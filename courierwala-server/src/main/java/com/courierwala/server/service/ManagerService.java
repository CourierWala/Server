package com.courierwala.server.service;

import com.courierwala.server.dto.GetStaffDto;

import java.util.List;

public interface ManagerService {
    void acceptDeliveryStaff(Long applicationId);

    List<GetStaffDto> getAllStaff(boolean isCurrentStaff);

    void rejectApplication(Long rejectApplicationId);
}
