package com.courierwala.server.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.courierwala.server.repository.DayCountProjection;
import com.courierwala.server.repository.DeliveryAssignmentRepository;
import com.courierwala.server.staffdto.DayAmountDto;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class DeliveryStatsService {
	private final DeliveryAssignmentRepository repository;

    public List<DayAmountDto> getWeeklyDeliveredOrders(Long staffId) {

        LocalDate today = LocalDate.now();

        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);

        List<DayCountProjection> result =
                repository.countWeeklyDeliveredOrders(
                        staffId,
                        weekStart.atStartOfDay(),
                        weekEnd.atTime(LocalTime.MAX)
                );

        // Initialize all days with 0
        Map<Integer, Long> dayMap = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            dayMap.put(i, 0L);
        }

        // Fill actual counts
        for (DayCountProjection row : result) {
            dayMap.put(row.getDayOfWeek(), row.getTotal());
        }

        return List.of(
                new DayAmountDto("Mon", dayMap.get(2)),
                new DayAmountDto("Tue", dayMap.get(3)),
                new DayAmountDto("Wed", dayMap.get(4)),
                new DayAmountDto("Thu", dayMap.get(5)),
                new DayAmountDto("Fri", dayMap.get(6)),
                new DayAmountDto("Sat", dayMap.get(7)),
                new DayAmountDto("Sun", dayMap.get(1))
        );
    }
}
