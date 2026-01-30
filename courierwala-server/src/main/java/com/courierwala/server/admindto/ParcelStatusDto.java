package com.courierwala.server.admindto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParcelStatusDto {
    private long successful;
    private long failed;
}

