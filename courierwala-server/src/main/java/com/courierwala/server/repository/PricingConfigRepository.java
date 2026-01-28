package com.courierwala.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.PricingConfig;

public interface PricingConfigRepository
        extends JpaRepository<PricingConfig, Long> {
}
