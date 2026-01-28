package com.courierwala.server.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "address")
@AttributeOverride(name = "id", column = @Column(name = "address_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Address extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)           
    private User user;

    private String streetAddress;
    private String pincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    
    private Double latitude;
    private Double longitude;


    private Boolean isDefault;
    
    public String getFullAddress() {
        String cityName = (city != null) ? city.getCityName() : "";
        return streetAddress + ", " + cityName;
    }

}

