package com.courierwala.server.service;

import org.jspecify.annotations.Nullable;

import com.courierwala.server.customerdto.LoginDTO;
import com.courierwala.server.entities.User;
import com.courierwala.server.staffdto.StaffSignupDto;
import com.courierwala.server.staffdto.staffProfileResponseDTO;

import jakarta.validation.Valid;

public interface StaffService {

	
	

	public void signUp(@Valid StaffSignupDto signupdto);

	

	public void login(@Valid LoginDTO loginDTO);



	public staffProfileResponseDTO getStaffProfile(Long staffId);

}
