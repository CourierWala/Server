package com.courierwala.server.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "delivery_staff_profile")
@AttributeOverride(name = "id", column = @Column(name = "staff_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DeliveryStaffProfile extends BaseEntity {

    @Column(unique = true)
    private String employeeCode;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String vehicleNumber;
    private String licenseNumber;

    private Double rating;
    private Integer totalDeliveries;
    private Boolean isVerified;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id")
    private User staff;
}
