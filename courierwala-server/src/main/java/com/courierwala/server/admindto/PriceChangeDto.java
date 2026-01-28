package com.courierwala.server.admindto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PriceChangeDto {
    @NotNull
    @PositiveOrZero
    private Double basePrice;

    @NotNull
    @PositiveOrZero
    private Double pricePerKm;

    @NotNull
    @PositiveOrZero
    private Double pricePerKg;
}
