package com.courierwala.server.entities;


import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "hub")
@AttributeOverride(name = "id", column = @Column(name = "hub_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Hub extends BaseEntity {

    @Column(name = "hub_name", nullable = false, unique = true)  //(hub_name, hub_city, latitude, longitude, manager_id)
    private String hubName;

    @Column(name = "hub_city", nullable = false)
    private String hubCity;   

    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "hub")
    private List<DeliveryStaffProfile> deliveryStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", unique = true)
    private User manager;
}
