package com.courierwala.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import com.courierwala.server.dto.RoutingResult;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.HubRoute;
import com.courierwala.server.repository.HubRepository;
import com.courierwala.server.repository.HubRouteRepository;
import com.courierwala.server.utils.ShipmentUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentRoutingService {

    private static final double DIRECT_DELIVERY_THRESHOLD_KM = 50.0;

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;

    public Hub findNearestHub(double lat, double lng) {
    	
        return hubRepository.findAll().stream()
            .min(Comparator.comparing(
                hub -> ShipmentUtils.calculateDistance(
                    lat, lng,
                    hub.getLatitude(), hub.getLongitude()
                )
            ))
            .orElseThrow(() -> new RuntimeException("No hub found"));
    }

    public RoutingResult decideRouting(
            double pickupLat, double pickupLng,
            double deliveryLat, double deliveryLng) {

    	System.out.println("in routing  deciding ============================================================================");
        Hub sourceHub = findNearestHub(pickupLat, pickupLng);
        
        System.out.println("source hub ======================== " + sourceHub.getHubName());
        
        double directDistance =
            ShipmentUtils.calculateDistance(
                sourceHub.getLatitude(),
                sourceHub.getLongitude(),
                deliveryLat,
                deliveryLng
            );
        
        System.out.println("Distance  ======================== " + directDistance);
    
        // Checking distance between source and destination is less that out set threshold distance
        if (ShipmentUtils.isDirectDelivery(
                directDistance,
                DIRECT_DELIVERY_THRESHOLD_KM)) {

            return RoutingResult.direct(sourceHub, directDistance);
        }

        Hub destinationHub = findNearestHub(deliveryLat, deliveryLng);
        
        System.out.println("destination hub ======================== " + destinationHub.getHubName());
        
        List<Hub> hubPath =
            findShortestPath(sourceHub, destinationHub);

        hubPath.forEach(h -> System.out.println("path list ==================== : " + h));
        
        double pathDistance = calculatePathDistance(hubPath);

        return RoutingResult.routed(
            sourceHub, destinationHub, hubPath, pathDistance
        );
    }

    /* ----------------- DIJKSTRA ----------------- */

    private List<Hub> findShortestPath(Hub start, Hub end) {

        Map<Long, Double> dist = new HashMap<>();
        Map<Long, Hub> prev = new HashMap<>();

        PriorityQueue<HubNode> pq =
            new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

        pq.add(new HubNode(start, 0));
        dist.put(start.getId(), 0.0);

        while (!pq.isEmpty()) {
            HubNode node = pq.poll();
            Hub current = node.hub;

            if (current.equals(end)) break;

            for (HubRoute route :
                    hubRouteRepository.findByFromHub(current)) {

                Hub next = route.getToHub();
                double newDist =
                    dist.get(current.getId()) + route.getDistanceKm();

                if (newDist < dist.getOrDefault(
                        next.getId(), Double.MAX_VALUE)) {

                    dist.put(next.getId(), newDist);
                    prev.put(next.getId(), current);
                    pq.add(new HubNode(next, newDist));
                }
            }
        }

        List<Hub> path = new ArrayList<>();
        for (Hub at = end; at != null; at = prev.get(at.getId())) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private double calculatePathDistance(List<Hub> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += ShipmentUtils.calculateDistance(
                path.get(i).getLatitude(), path.get(i).getLongitude(),
                path.get(i + 1).getLatitude(), path.get(i + 1).getLongitude()
            );
        }
        return total;
    }

    /* ----------------- HELPER CLASSES ----------------- */

    private static class HubNode {
        Hub hub;
        double distance;
        
        HubNode(Hub hub, double distance) {
            this.hub = hub;
            this.distance = distance;
        }
    }
}
