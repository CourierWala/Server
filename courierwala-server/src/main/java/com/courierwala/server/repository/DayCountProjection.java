package com.courierwala.server.repository;

public interface DayCountProjection {
    Integer getDayOfWeek(); // 1 = Sunday, 2 = Monday ...
    Long getTotal();
}

