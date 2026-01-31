package com.courierwala.server.managerdto;

import com.courierwala.server.enumfield.OrderStatus;

public class OrderStatusCountDto {

    private OrderStatus status;
    private Long count;

    public OrderStatusCountDto(OrderStatus status, Long count) {
        this.status = status;
        this.count = count;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Long getCount() {
        return count;
    }
}
