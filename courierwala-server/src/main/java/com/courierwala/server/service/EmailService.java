package com.courierwala.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.courierwala.server.dto.SendEmailDTO;
import com.courierwala.server.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final RestClient restClient;	
	@Value("${emailservice.url}")
    private String emailUrl;
	private String msg;
	public void sendEmail(SendEmailDTO emailBody) {
	
		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    
		System.out.println("email : " + user.getUsername());
		if(emailBody.getTo()==null) {			
			emailBody.setTo(user.getUsername());
		}
		if(user != null) {
		msg = "Hello "+user.getName()+",\n\t"+ emailBody.getMessage();
//		msg = "Hello " + emailBody.getName() + ", \n \t" + emailBody.getMessage();
		
		emailBody.setMessage(msg);
		}
		try {
            restClient.post()
                    .uri(emailUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(emailBody)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            System.out.printf("Email service failed", ex);
        }
	}
}
