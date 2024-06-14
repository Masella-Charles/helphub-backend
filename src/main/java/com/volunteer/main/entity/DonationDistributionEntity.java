package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="DISASTERS")
@Data
public class DonationDistributionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISASTERS_REQ")
    @SequenceGenerator(name = "DISASTERS_REQ", sequenceName = "DISASTERS_REQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long tId;

    private String recipientName;
    private Double amountDistributed;
    private Integer quantityDistributed;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private DonationEntity donation;
}
