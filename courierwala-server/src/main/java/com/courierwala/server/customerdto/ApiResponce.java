package com.courierwala.server.customerdto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponce {
	
	private String status;
	private String message;
	private LocalDateTime timeStamp;
	
	public ApiResponce(String status, String message) {
		this.status = status;
		this.message = message;
		timeStamp = LocalDateTime.now();
	}
	

}
