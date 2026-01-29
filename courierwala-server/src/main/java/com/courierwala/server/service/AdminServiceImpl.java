package com.courierwala.server.service;

import java.util.ArrayList;
import java.util.List;

import com.courierwala.server.admindto.*;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.PricingConfig;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.repository.HubRepository;
import com.courierwala.server.repository.PricingConfigRepository;
import com.courierwala.server.repository.UserRepository;
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
    private final PricingConfigRepository pricingConfigRepository;

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



    @Override
    public void changePrice(PriceChangeDto dto) {

        PricingConfig pricing = pricingConfigRepository
                .findById(1L)
                .orElse(new PricingConfig());

        pricing.setBasePrice(dto.getBasePrice());
        pricing.setPricePerKm(dto.getPricePerKm());
        pricing.setPricePerKg(dto.getPricePerKg());

        pricingConfigRepository.save(pricing);
    }

    @Override
    public PriceChangeDto getPriceConfig() {

        PricingConfig pricing = pricingConfigRepository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException("Pricing config not found"));

        PriceChangeDto dto = new PriceChangeDto();
        dto.setBasePrice(pricing.getBasePrice());
        dto.setPricePerKm(pricing.getPricePerKm());
        dto.setPricePerKg(pricing.getPricePerKg());

        return dto;
    }

    // =================================================
    //             HUB SERVICES
    // =================================================

    @Override
    public List<HubDetailsDto> getAllHubs() {
        List<Hub> hubs = hubRepository.findAll();
        List<HubDetailsDto> hubDetails = new ArrayList<>();

        for (Hub hub : hubs) {
            HubDetailsDto dto = HubDetailsDto.builder()
                    .hubId(hub.getId())
                    .hubName(hub.getHubName())
                    .hubCity(hub.getHubCity())
                    .managerId(hub.getManager().getId())
                    .managerName(hub.getManager().getName())
                    .managerEmail(hub.getManager().getEmail())
                    .managerPhone(hub.getManager().getPhone())
                    .build();

            hubDetails.add(dto);
        }

        return hubDetails;
    }

    public AdminProfileUpdateDto getAdminProfile(Long adminId) {

        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Ensure this user is ADMIN
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new RuntimeException("Access denied: Not an admin");
        }

        AdminProfileUpdateDto dto = new AdminProfileUpdateDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }
}
