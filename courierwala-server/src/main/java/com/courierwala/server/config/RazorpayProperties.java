package com.courierwala.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "razorpay.key")
@Getter
@Setter
public class RazorpayProperties {

    private String id;
    private String secret;
}

