package com.courierwala.server.controller;

import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.dto.LoginResDTO;
import com.courierwala.server.dto.SendEmailDTO;
import com.courierwala.server.service.AuthService;
import com.courierwala.server.service.EmailService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
	
	private final RestClient restClient;	
	@Value("${emailservice.url}")
    private String emailUrl;

	private final AuthService authService;

	// ================= SIGN UP =================
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signupdto) {

		ApiResponse response = authService.signUp(signupdto);
		SendEmailDTO email = new SendEmailDTO(null,null);
		email.setTo(signupdto.getEmail());
		email.setSubject("SignUp SuccessFull");
		email.setMessage(signupdto.getName()+"\nWelcome To CourierWala!!");
		
		try {
            restClient.post()
                    .uri(emailUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(email)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            System.out.printf("Email service failed", ex);
        }

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ================= LOGIN =================
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse responce) {

		LoginResDTO res = authService.login(loginDTO, responce);

		System.out.println("In login !!");
		return ResponseEntity.ok(res);

	}
	

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

    	System.out.println("logout route !!");
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // true if HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); //  delete cookie

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

}
