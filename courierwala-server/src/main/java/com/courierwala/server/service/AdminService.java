package com.courierwala.server.service;

import com.courierwala.server.admindto.AdminProfileUpdateDto;
import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.admindto.ManagerUpdateDto;
import com.courierwala.server.admindto.PriceChangeDto;

import java.util.List;

public interface AdminService {
    List<ManagerDetailsDto> getManagerDetails();
    void updateManagerDetails(Long hubId, ManagerUpdateDto dto);

    void updateAdminProfile(Long adminId, AdminProfileUpdateDto dto);
    void changePrice(PriceChangeDto dto);
    PriceChangeDto getPriceConfig();



}


