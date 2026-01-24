package com.courierwala.server.repository;

import java.util.List;

import com.courierwala.server.admindto.ManagerDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

 import com.courierwala.server.entities.Hub;

public interface HubRepository extends JpaRepository<Hub, Long> {

    @Query("""
    SELECT new com.courierwala.server.admindto.ManagerDetailsDto(
        u.name,
        u.email,
        h.hubName
    )
    FROM Hub h
    JOIN h.manager u
""")
    List<ManagerDetailsDto> findAllManagerDetails();

}
