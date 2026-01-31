package com.courierwala.server.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetStaffDto {
    /*
      id: "1",
      name: "Sarah Johnson",
      email: "sarah.j@Courier-wala.com",
      phone: "+1 (555) 123-4567",
      location: "New York Hub",
      vehicle_type: "Car",
      vehicle_num: "4444"
    * */

    private Long Id;
    private String Name;
    private String Email;
    private String Phone;
    private String Location;
    private String VehicleType;
    private String VehicleNum;
}
