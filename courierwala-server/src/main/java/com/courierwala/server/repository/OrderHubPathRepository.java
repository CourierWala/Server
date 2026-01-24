package com.courierwala.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.OrderHubPath;

public interface OrderHubPathRepository extends JpaRepository<OrderHubPath, Long> {

	List<OrderHubPath> findByOrderOrderBySequenceAsc(CourierOrder order);

	Optional<OrderHubPath> findFirstByOrderAndCompletedFalseOrderBySequenceAsc(CourierOrder order);

//	void saveAll(List<OrderHubPath> paths);
	
	

}
