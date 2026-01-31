package com.courierwala.server.managerdto;

public class StaffLoadDto {

    private String name;
    private Integer activeOrders;

    public StaffLoadDto(String name, Integer activeOrders) {
        this.name = name;
        this.activeOrders = activeOrders;
    }

    public String getName() {
        return name;
    }

    public Integer getActiveOrders() {
        return activeOrders;
    }
}
