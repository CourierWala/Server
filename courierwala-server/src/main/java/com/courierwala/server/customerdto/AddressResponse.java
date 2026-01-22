package com.courierwala.server.customerdto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {

    private Long addressId;
    private String addressLine;
    private String pincode;
    private String cityName;
    private Boolean isDefault;
}
