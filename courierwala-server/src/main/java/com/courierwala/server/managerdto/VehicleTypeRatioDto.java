package com.courierwala.server.managerdto;

import com.courierwala.server.enumfield.VehicleType;

public class VehicleTypeRatioDto {

    private VehicleType vehicleType;
    private Long count;

    public VehicleTypeRatioDto(VehicleType vehicleType, Long count) {
        this.vehicleType = vehicleType;
        this.count = count;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Long getCount() {
        return count;
    }
}
