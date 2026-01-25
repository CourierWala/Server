package com.courierwala.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.OrderHubPath;
import com.courierwala.server.repository.OrderHubPathRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderHubPathService {

    private final OrderHubPathRepository repository;

    // saving the path of order in Order Hub Path to know which Sequence order is going
    @Transactional
    public void savePath(CourierOrder order, List<Hub> hubs) {

        List<OrderHubPath> paths = new ArrayList<>();
        
        hubs.forEach((h) -> System.out.println("hub path : " + h));

        for (int i = 0; i < hubs.size(); i++) {

            OrderHubPath path = OrderHubPath.builder()
                    .order(order)
                    .hub(hubs.get(i))
                    .sequence(i)
                    .completed(false) 
                    .build();

            paths.add(path);
        }

        repository.saveAll(paths);
        
        
        
        
    }
}

