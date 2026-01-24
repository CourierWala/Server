package com.courierwala.server.service;

import com.courierwala.server.admindto.ManagerDetailsDto;

import java.util.List;

public interface AdminService {
    List<ManagerDetailsDto> getManagerDetails();
}
