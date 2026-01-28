package com.courierwala.server.service;

import java.util.ArrayList;
import java.util.List;

import com.courierwala.server.admindto.AddManagerDto;
import com.courierwala.server.admindto.AdminProfileUpdateDto;
import com.courierwala.server.admindto.ManagerDetailsDto;
import com.courierwala.server.admindto.ManagerUpdateDto;
import com.courierwala.server.entities.Address;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.repository.HubRepository;
import com.courierwala.server.repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final HubRepository hubRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ManagerDetailsDto> getManagerDetails() {
        return hubRepository.findAllManagerDetails();
    }

    @Override
    public void updateManagerDetails(Long hubId, ManagerUpdateDto dto) {

        Hub hub = hubRepository.findByIdAndManagerIsNotNull(hubId)
                .orElseThrow(() -> new RuntimeException("Hub or manager not found"));

        User manager = hub.getManager();

        //  SAFETY CHECK
        if (manager.getRole() != Role.ROLE_STAFF_MANAGER) {
            throw new RuntimeException("User is not a staff manager");
        }

        //  ONLY ALLOWED FIELDS
        manager.setName(dto.getName());
        manager.setEmail(dto.getEmail());
        manager.setPhone(dto.getPhone());

    }

    @Override
    public void addManager(AddManagerDto manager) {
        User user = new User();
        user.setName(manager.getManagerName());
        user.setEmail(manager.getManagerEmail());
        user.setPassword(passwordEncoder.encode("Pass@1234"));
        user.setPhone(manager.getManagerPhone());
        user.setRole(Role.ROLE_STAFF_MANAGER);
        user.setStatus(manager.getManagerStatus());
        user.setAddresses(null);
        userRepository.save(user);
    }

    @Override
    public void updateAdminProfile(Long adminId, AdminProfileUpdateDto dto) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // ONLY update allowed fields
        if (dto.getName() != null) {
            admin.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            admin.setEmail(dto.getEmail());
        }
    }
}
