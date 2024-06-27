package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="DISASTERS")
@Data
public class DisasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISASTERS_REQ")
    @SequenceGenerator(name = "DISASTERS_REQ", sequenceName = "DISASTERS_REQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Boolean status;
}
