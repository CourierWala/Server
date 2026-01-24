package com.courierwala.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.Payment;

public interface PaymentRepository
extends JpaRepository<Payment, Long> {

Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}

