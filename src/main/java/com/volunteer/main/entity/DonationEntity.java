package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="DONATION")
@Data
public class DonationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DONATION_REQ")
    @SequenceGenerator(name = "DONATION_REQ", sequenceName = "DONATION_REQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long id;
    private String donorName;
    private String type; // 'money' or 'commodity'

    // Fields for money donations
    private Double amount;

    // Fields for commodity donations
    private String commodityName;
    private Integer quantity;
    private Boolean status;

    @ManyToOne(optional = true)
    @JoinColumn(name = "disaster_id", referencedColumnName = "T_ID")
    private DisasterEntity disasterEntity;
}
