package com.courierwala.server.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public boolean verifySignature(
            String orderId,
            String paymentId,
            String razorpaySignature) {

        try {
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey =
                    new SecretKeySpec(
                        razorpayKeySecret.getBytes(),
                        "HmacSHA256"
                    );

            mac.init(secretKey);
            byte[] hash = mac.doFinal(payload.getBytes());

            String generatedSignature =
                    Base64.getEncoder().encodeToString(hash);

            return generatedSignature.equals(razorpaySignature);

        } catch (Exception e) {
            return false;
        }
    }
    
    @Transactional
    public void markPaymentSuccess(
            String orderId,
            String paymentId) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(orderId)
                .orElseThrow(() ->
                    new RuntimeException("Payment not found"));

        payment.setRazorpayPaymentId(paymentId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        paymentRepository.save(payment);
    }
}

