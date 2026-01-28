package com.courierwala.server.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pricing_config")
public class PricingConfig extends BaseEntity{
    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double pricePerKm;

    @Column(nullable = false)
    private Double pricePerKg;
}
