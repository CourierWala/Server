package com.courierwala.server.managerdto;

import java.time.LocalDateTime;

public class DispatchTrendDto {

    private LocalDateTime departureTime;
    private Long count;

    public DispatchTrendDto(LocalDateTime departureTime, Long count) {
        this.departureTime = departureTime;
        this.count = count;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Long getCount() {
        return count;
    }
}
