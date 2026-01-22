package com.courierwala.server.entities;

import com.courierwala.server.enumfield.AssignedBy;
import com.courierwala.server.enumfield.DeliveryStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "delivery_assignment")
@AttributeOverride(name = "id", column = @Column(name = "assignment_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DeliveryAssignment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private CourierOrder order;

    @ManyToOne
    @JoinColumn(name = "delivery_staff_id")
    private DeliveryStaffProfile deliveryStaff;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Enumerated(EnumType.STRING)
    private AssignedBy assignedBy;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
}

