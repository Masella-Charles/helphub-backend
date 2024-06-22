package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="VOLUNTEERING_OPPORTUNITIES")
@Data
public class OpportunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOL_OPP_SEQ")
    @SequenceGenerator(name = "VOL_OPP_SEQ", sequenceName = "VOL_OPP_SEQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long tId;

    private String name;
    private String description;
    private LocalDate date;
    private Integer requiredVolunteers;
    private Double hours;
    private Boolean status;

    @ManyToOne(optional = true)
    @JoinColumn(name = "disaster_id", referencedColumnName = "T_ID")
    private DisasterEntity disasterEntity;
}
