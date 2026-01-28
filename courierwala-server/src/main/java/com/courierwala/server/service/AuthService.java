package com.courierwala.server.service;

import com.courierwala.server.customerdto.SignUpDTO;
import com.courierwala.server.dto.ApiResponse;
import com.courierwala.server.dto.LoginDTO;
import com.courierwala.server.dto.LoginResDTO;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	
	ApiResponse signUp(SignUpDTO dto);

	LoginResDTO login(LoginDTO loginDTO, HttpServletResponse response);
}
