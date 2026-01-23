package com.courierwala.server.customerdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerProfileUpdateDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;
}
