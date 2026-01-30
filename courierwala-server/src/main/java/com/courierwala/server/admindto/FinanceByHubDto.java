package com.courierwala.server.admindto;

import java.math.BigDecimal;

public class FinanceByHubDto {

    private String hub;
    private BigDecimal revenue;
    private BigDecimal expenses;

    public FinanceByHubDto(String hub, BigDecimal revenue, BigDecimal expenses) {
        this.hub = hub;
        this.revenue = revenue;
        this.expenses = expenses;
    }

    public String getHub() {
        return hub;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }
}
