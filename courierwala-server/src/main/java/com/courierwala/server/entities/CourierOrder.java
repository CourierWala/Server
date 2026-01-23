package com.courierwala.server.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.courierwala.server.enumfield.DeliveryType;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.enumfield.PackageSize;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courier_order")
@AttributeOverride(
    name = "id",
    column = @Column(name = "order_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierOrder extends BaseEntity {

   

    @Column(unique = true, nullable = false)
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
                                 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_address_id", nullable = false)
    private Address pickupAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;


    private BigDecimal packageWeight;
    private String packageType;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    private PackageSize packageSize;
    
    private Double distanceKm;
    private BigDecimal price;
 
    private LocalDate pickupDate;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    @Lob
    @Column(nullable = true)
    private String packageDescription;

    @Version
    private Long version;
}

