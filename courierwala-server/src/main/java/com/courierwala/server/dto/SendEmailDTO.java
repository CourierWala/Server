package com.courierwala.server.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendEmailDTO {
	private String to;
	private String subject;
	private String message;
	public SendEmailDTO(String subject, String message) {
		this.subject = subject;
		this.message = message;
	}
	
	
	
}
