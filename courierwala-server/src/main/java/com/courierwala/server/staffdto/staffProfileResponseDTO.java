
package com.courierwala.server.staffdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class staffProfileResponseDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private String address;

    private String vehicleType;
    private String vehicleNumber;
}
