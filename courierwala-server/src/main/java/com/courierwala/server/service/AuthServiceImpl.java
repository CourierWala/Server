package com.courierwala.server.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.dto.LoginResDTO;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import com.courierwala.server.jwtutils.JwtUtil;
import com.courierwala.server.repository.UserRepository;
import com.courierwala.server.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // ================= SIGN UP =================
    @Override
    public ApiResponse signUp(SignUpDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .role(Role.ROLE_CUSTOMER)
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);

        return new ApiResponse("success", "Customer registered successfully");
    }

    // ================= LOGIN =================
    @Override
    public LoginResDTO login(LoginDTO loginDTO, HttpServletResponse response) {

        System.out.println("in login !!");
        // 1️ Authenticate user (email + password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        // 2️ Get authenticated user
        // IMPORTANT: User must implement UserDetails
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        if (user != null) {
            User user1 = userRepository
                    .findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid user details"));

            if(user1.getRole() == Role.ROLE_DELIVERY_STAFF && user1.getStatus() == Status.INACTIVE){
                return new LoginResDTO(
                        "failure",
                        "User not found",
                        null,
                        null,
                        null
                );
            }
        }

        // 3 Generate JWT (contains id, email, role)
        String token = jwtUtil.generateToken(user);

        // 4 Store JWT in HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                .httpOnly(true)
                .secure(false)        // true in production (HTTPS)
                .sameSite("Lax")      // use "None" + Secure(true) for cross-domain prod
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 5 Send safe user info to frontend
        return new LoginResDTO(
                "success",
                "Login successful",
                user.getId(),
                user.getUsername(),
                user.getAuthorities().toString()
        );
    }
}
