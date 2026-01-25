package com.courierwala.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.HubRoute;

public interface HubRouteRepository extends JpaRepository<HubRoute, Long> {

	HubRoute[] findByFromHub(Hub current);

}
