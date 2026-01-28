package com.courierwala.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResDTO {
	
	private String status;
	private String message;
	private Long id;
	private String email;
	private String role;
	private String timestamp;
	public LoginResDTO(String status, String message, Long id, String email, String role) {
		this.status = status;
		this.message = message;
		this.id = id;
		this.email = email;
		this.role = role;
	}
	

}
