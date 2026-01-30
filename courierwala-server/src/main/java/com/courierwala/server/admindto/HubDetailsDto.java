package com.courierwala.server.admindto;

import com.courierwala.server.entities.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class HubDetailsDto {
    private long hubId;

    private String hubName;

    private String hubCity;

    private Long managerId;

    private String managerName;

    private String managerEmail;

    private String managerPhone;
}
