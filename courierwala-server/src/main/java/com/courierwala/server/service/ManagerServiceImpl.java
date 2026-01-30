package com.courierwala.server.service;

import com.courierwala.server.dto.GetStaffDto;
import com.courierwala.server.entities.DeliveryStaffProfile;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.repository.DeliveryStaffProfileRepository;
import com.courierwala.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {
    private final DeliveryStaffProfileRepository deliveryStaffProfileRepository;
    private final UserRepository userRepository;

    @Override
    public void acceptDeliveryStaff(Long applicationId) {
        DeliveryStaffProfile staff = deliveryStaffProfileRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Delivery staff application not found"));

        staff.getUser().setStatus(Status.ACTIVE);
        staff.setIsAvailable(true);
        staff.setIsVerified(true);

        deliveryStaffProfileRepository.save(staff);
    }

    @Override
    public List<GetStaffDto> getAllStaff(boolean isCurrentStaff) {
        List<DeliveryStaffProfile> staff = deliveryStaffProfileRepository.findAllByIsVerified(isCurrentStaff);

        List<GetStaffDto> res = new ArrayList<>();

        for(DeliveryStaffProfile s : staff) {
            GetStaffDto dto = GetStaffDto.builder()
                    .Id(s.getId())
                    .Name(s.getUser().getName())
                    .Email(s.getUser().getEmail())
                    .Phone(s.getUser().getPhone())
                    .Location(s.getHub().getHubCity())
                    .VehicleType(String.valueOf(s.getVehicleType()))
                    .VehicleNum(s.getVehicleNumber())
                    .build();
            res.add(dto);
        }

        return res;
    }
}
