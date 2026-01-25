package com.courierwala.server.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.server.dto.PaymentVerificationRequest;
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
    
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody PaymentVerificationRequest request) {

        boolean isValid = paymentService.verifySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid payment signature");
        }

        paymentService.markPaymentSuccess(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId()
        );

        return ResponseEntity.ok("Payment verified successfully");
    }

}

