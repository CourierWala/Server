package com.courierwala.server.service;

import com.courierwala.server.admindto.*;

import java.util.List;

public interface AdminService {
    List<ManagerDetailsDto> getManagerDetails();

    void updateManagerDetails(Long managerId, ManagerUpdateDto dto);

    void updateAdminProfile(Long adminId, AdminProfileUpdateDto dto);

    void addManager(AddManagerDto manager);

    void changePrice(PriceChangeDto dto);
    PriceChangeDto getPriceConfig();

    List<HubDetailsDto> getAllHubs();

    AdminProfileUpdateDto getAdminProfile(Long adminId);

}


