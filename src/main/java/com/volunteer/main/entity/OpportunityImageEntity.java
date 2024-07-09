package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="OPPORTUNITY_IMAGES")
@Data
public class OpportunityImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPP_IMG_SEQ")
    @SequenceGenerator(name = "OPP_IMG_SEQ", sequenceName = "OPP_IMG_SEQ", allocationSize = 1)
    @Column(name = "T_ID")
    private Long id;


    private String fileName;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] imageData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "opportunity_id", referencedColumnName = "T_ID")
    private OpportunityEntity opportunityEntity;


}