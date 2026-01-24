package com.courierwala.server.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.server.entities.Payment;
import com.courierwala.server.service.PaymentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Payment> create(
            @RequestParam BigDecimal amount)
            throws RazorpayException {

        Payment payment =
                paymentService.createPaymentOrder(amount);

        return ResponseEntity.ok(payment);
    }
}

