package com.courierwala.server.dto;

import java.util.List;

import com.courierwala.server.entities.Hub;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutingResult {

    private boolean direct;
    private Hub sourceHub;
    private Hub destinationHub;
    private List<Hub> hubPath;
    private double distanceKm;

    public static RoutingResult direct(Hub sourceHub, double distance) {
        return new RoutingResult(true, sourceHub, null, null, distance);
    }

    public static RoutingResult routed(
            Hub sourceHub,
            Hub destinationHub,
            List<Hub> path,
            double distance) {

        return new RoutingResult(false, sourceHub, destinationHub, path, distance);
    }
}

