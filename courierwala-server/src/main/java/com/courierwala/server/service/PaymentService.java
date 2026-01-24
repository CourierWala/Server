package com.courierwala.server.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courierwala.server.entities.Payment;
import com.courierwala.server.enumfield.PaymentStatus;
import com.courierwala.server.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPaymentOrder(BigDecimal amount)
            throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("amount", amount.multiply(BigDecimal.valueOf(100)) );
        options.put("currency", "INR");
        options.put("receipt", "receipt_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(options);

        Payment payment = new Payment();
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.CREATED);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }
}

