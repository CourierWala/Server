package com.courierwala.server.service;

import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.admindto.ManagerUpdateDto;

import java.util.List;

public interface AdminService {
    List<ManagerDetailsDto> getManagerDetails();
    void updateManagerDetails(Long hubId, ManagerUpdateDto dto);

}
