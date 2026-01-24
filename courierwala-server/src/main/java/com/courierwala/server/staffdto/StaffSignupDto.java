
package com.courierwala.server.staffdto;
import com.courierwala.server.enumfield.VehicleType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffSignupDto {

    /*  USER FIELDS */

    @NotBlank(message = "Full name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /*  STAFF PROFILE FIELDS  */

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotNull(message = "Hub is required")
    private Long hubId;   // FK reference to Hub
}

