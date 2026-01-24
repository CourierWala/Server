package com.courierwala.server.service;

import java.util.List;

import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.repository.HubRepository;
import org.springframework.stereotype.Service;


import com.courierwala.server.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final HubRepository hubRepository;

    @Override
    public List<ManagerDetailsDto> getManagerDetails() {
        return hubRepository.findAllManagerDetails();
    }
}
