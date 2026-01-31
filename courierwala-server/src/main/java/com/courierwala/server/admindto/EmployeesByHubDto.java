package com.courierwala.server.admindto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeesByHubDto {
    private String hub;
    private long delivery;
    private long warehouse;
    private long support;
}

