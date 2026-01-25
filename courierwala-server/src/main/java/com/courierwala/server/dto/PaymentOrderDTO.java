package com.courierwala.server.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentOrderDTO {

	private String razorpayOrderId;
	private BigDecimal amount;
}
