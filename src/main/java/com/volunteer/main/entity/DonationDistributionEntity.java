package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="DONATION_DISTRIBUTION")
@Data
public class DonationDistributionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DONATION_DISTRIBUTION_REQ")
    @SequenceGenerator(name = "DONATION_DISTRIBUTION_REQ", sequenceName = "DONATION_DISTRIBUTION_REQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long id;

    private String recipientName;
    private Double amountDistributed;
    private Integer quantityDistributed;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private DonationEntity donation;
}
