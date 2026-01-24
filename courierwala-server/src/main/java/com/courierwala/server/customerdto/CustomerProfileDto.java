package com.courierwala.server.customerdto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerProfileDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<AddressResponse> addresses;
}
