package com.courierwala.server.entities;

import java.time.LocalDateTime;

import com.courierwala.server.enumfield.TicketStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "support_ticket")
@AttributeOverride(name = "id", column = @Column(name = "ticket_id"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SupportTicket extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private CourierOrder order;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    private String issueType;
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime resolvedAt;
}

