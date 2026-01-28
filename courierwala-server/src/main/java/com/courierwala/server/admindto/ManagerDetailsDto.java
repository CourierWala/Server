package com.courierwala.server.admindto;

import com.courierwala.server.enumfield.Role;
import com.courierwala.server.enumfield.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManagerDetailsDto {

    private Long managerId;
    private String managerName;
    private String managerEmail;
    private String managerPhone;
    private Role managerRole;
    private Status managerStatus;
    private String hubName;
}
