package com.courierwala.server.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.courierwala.server.enumfield.PaymentMode;
import com.courierwala.server.enumfield.PaymentStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payment")
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class Payment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private CourierOrder order;
    
 // Razorpay order ID (order_xxx)
    @Column(nullable = false, unique = true)
    private String razorpayOrderId;
    
 // Filled only after success
    private String razorpayPaymentId;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

 // CREATED, SUCCESS, FAILED
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
}

