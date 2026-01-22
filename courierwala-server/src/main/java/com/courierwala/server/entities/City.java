package com.courierwala.server.entities;


import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "city")
@AttributeOverride(name = "id", column = @Column(name = "city_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class City extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String cityName;

    private String state;
    private Double latitude;
    private Double longitude;

    @OneToOne
    @JoinColumn(name = "hub_id")
    private Hub hubs;

    @OneToMany(mappedBy = "city")
    private List<Address> addresses;
}

