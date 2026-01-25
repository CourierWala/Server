package com.courierwala.server.customerdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShipmentResDto {
	
	
	private Long order_id;
	private String message;
	private String status;
}
