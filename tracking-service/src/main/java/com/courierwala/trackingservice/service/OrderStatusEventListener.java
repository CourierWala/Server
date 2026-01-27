package com.courierwala.trackingservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courierwala.trackingservice.config.RabbitMQConfig;
import com.courierwala.trackingservice.dto.OrderStatusEvent;
import com.courierwala.trackingservice.entities.OrderStatus;
import com.courierwala.trackingservice.entities.OrderStatusHistory;
import com.courierwala.trackingservice.repository.OrderStatusHistoryRepository;

@Service
public class OrderStatusEventListener {

    @Autowired
    private OrderStatusHistoryRepository repository;

    @RabbitListener(queues = RabbitMQConfig.TRACKING_QUEUE)
    public void consumeOrderStatusEvent(OrderStatusEvent event) {

        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(event.getOrderId())
                .status(OrderStatus.valueOf(event.getStatus()))
                .remarks(event.getRemarks())
                .updatedByUserId(event.getUpdatedBy())
                .build();

        repository.save(history);
    }
}
