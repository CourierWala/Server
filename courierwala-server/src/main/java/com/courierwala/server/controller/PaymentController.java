package com.courierwala.server.controller;

import java.math.BigDecimal;

import com.courierwala.server.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.courierwala.server.dto.PaymentOrderDTO;
import com.courierwala.server.dto.PaymentVerificationRequest;
import com.courierwala.server.dto.SendEmailDTO;
import com.courierwala.server.entities.Payment;
import com.courierwala.server.service.EmailService;
import com.courierwala.server.service.PaymentService;
import com.razorpay.RazorpayException;

@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowCredentials = "true"
	)
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam BigDecimal amount, @RequestParam Long order_id)
            throws RazorpayException {

        Payment payment =
                paymentService.createPaymentOrder(amount,order_id);
        PaymentOrderDTO pdto = new PaymentOrderDTO();
        pdto.setRazorpayOrderId(payment.getRazorpayOrderId());
        pdto.setAmount(payment.getAmount());
        System.out.println(payment.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(pdto);
    }
    
    @PostMapping("/verify/{order_id}")
    public ResponseEntity<String> verifyPayment(@PathVariable Long order_id,
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

        String trackingNumber = customerService.getTrackingNumberById(order_id);
        System.out.println("tracking number : " + trackingNumber);
        emailService.sendEmail(new SendEmailDTO("Order Placed", "Your Order has been registered Successfully!!!.\nHere is your tracking no for future reference: "+trackingNumber+"\n\nOur Delivery Partner will Contact you soon."));

        return ResponseEntity.ok("Payment verified successfully");
    }

}

