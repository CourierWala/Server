package com.courierwala.server.admindto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManagerDetailsDto {

    private Long managerId;
    private String managerName;
    private String managerEmail;
    private String hubName;
}
