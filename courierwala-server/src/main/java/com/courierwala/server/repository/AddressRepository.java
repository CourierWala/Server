package com.courierwala.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{
	

}
