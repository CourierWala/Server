package com.courierwala.server.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.Payment;
import com.courierwala.server.enumfield.PaymentStatus;
import com.courierwala.server.repository.CourierOrderRepository;
import com.courierwala.server.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;
    
    @Autowired
    private CourierOrderRepository courierOrderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    
    public Payment createPaymentOrder(BigDecimal amount, Long order_id)
            throws RazorpayException {
    	System.out.println("=================================================================================================================================");
    	System.out.println("=================================================================================================================================");
    	
        JSONObject options = new JSONObject();
        options.put("amount", amount.multiply(BigDecimal.valueOf(100)) );
        options.put("currency", "INR");
        options.put("receipt", "receipt_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(options);
        
        CourierOrder courierOrder = courierOrderRepository.findById(order_id).orElseThrow();
        courierOrder.setPaymentStatus(PaymentStatus.CREATED);
        
        Payment payment = new Payment();
        payment.setRazorpayOrderId(order.get("id"));
        payment.setOrder(courierOrder);
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.CREATED);
        payment.setCreatedAt(LocalDateTime.now());
        courierOrderRepository.save(courierOrder);
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

            // 🔴 IMPORTANT: Convert to HEX (not Base64)
            String generatedSignature = bytesToHex(hash);
            
            return generatedSignature.equals(razorpaySignature);

        } catch (Exception e) {
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            String hexChar = Integer.toHexString(0xff & b);
            if (hexChar.length() == 1) {
                hex.append('0');
            }
            hex.append(hexChar);
        }
        return hex.toString();
    }
    
    @Transactional
    public void markPaymentSuccess(
            String orderId,
            String paymentId) {
    	System.out.println("================================== Inside Mark Payment Success FUNC");
        Payment payment = paymentRepository
                .findByRazorpayOrderId(orderId)
                .orElseThrow(() ->
                    new RuntimeException("Payment not found"));

        payment.getOrder().setPaymentStatus(PaymentStatus.SUCCESS);;
        payment.setRazorpayPaymentId(paymentId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        
    }
}

