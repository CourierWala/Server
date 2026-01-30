package com.courierwala.trackingservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    // Exchange
    public static final String ORDER_EXCHANGE = "order.exchange";

    // Queues
    public static final String TRACKING_QUEUE = "tracking.order.status.queue";
    public static final String SHIPMENT_CREATED_QUEUE = "tracking.shipment.created.queue";

    // Routing keys
    public static final String ORDER_STATUS_ROUTING_KEY = "order.status.changed";
    public static final String SHIPMENT_CREATED_ROUTING_KEY = "shipment.created";

    // -------- QUEUES --------

    @Bean
    public Queue trackingQueue() {
        return new Queue(TRACKING_QUEUE, true);
    }

    @Bean
    public Queue shipmentCreatedQueue() {
        return new Queue(SHIPMENT_CREATED_QUEUE, true);
    }

    // -------- EXCHANGE --------

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // -------- BINDINGS --------

    @Bean
    public Binding orderStatusBinding() {
        return BindingBuilder
                .bind(trackingQueue())
                .to(orderExchange())
                .with(ORDER_STATUS_ROUTING_KEY);
    }

    @Bean
    public Binding shipmentCreatedBinding() {
        return BindingBuilder
                .bind(shipmentCreatedQueue())
                .to(orderExchange())
                .with(SHIPMENT_CREATED_ROUTING_KEY);
    }

    // -------- JSON CONVERTER --------

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // -------- LISTENER FACTORY (VERY IMPORTANT) --------

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
