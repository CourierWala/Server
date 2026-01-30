package com.courierwala.server.events;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courierwala.server.config.RabbitMQConfig;
import com.courierwala.server.dto.OrderStatusEvent;
import com.courierwala.server.dto.ShipmentCreatedEvent;

@Service
public class OrderEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishOrderStatusEvent(OrderStatusEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_STATUS_ROUTING_KEY,
                event
        );
    }
    
    public void publishShipmentCreatedEvent(ShipmentCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.SHIPMENT_CREATED_KEY,
                event
        );
    }
}

