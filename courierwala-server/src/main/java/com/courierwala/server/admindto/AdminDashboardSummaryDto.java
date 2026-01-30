package com.courierwala.server.admindto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminDashboardSummaryDto {

    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private Long activeHubs;
    private Long managers;
}


