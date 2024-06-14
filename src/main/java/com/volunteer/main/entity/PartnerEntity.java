package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="PARTNERS")
@Data
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTNERS_REQ")
    @SequenceGenerator(name = "PARTNERS_REQ", sequenceName = "PARTNERS_REQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long tId;
    private String name;
    private String phone;
    private String email;
    private String additionalInfo;
    private Boolean status;
}
