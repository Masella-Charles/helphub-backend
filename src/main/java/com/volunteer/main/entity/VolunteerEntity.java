package com.volunteer.main.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="VOLUNTEERS")
@Data
public class VolunteerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEERS_REQ")
    @SequenceGenerator(name = "VOLUNTEERS_REQ", sequenceName = "VOLUNTEERS_REQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long tId;
    private String name;
    private String phone;
    private String email;
    private String additionalInfo;
    private Boolean status;
}
