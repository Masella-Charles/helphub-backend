package com.volunteer.main.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "T_ID")
    @JsonManagedReference
    private UserEntity user;

    @Column(name = "ADDITIONAL_INFO")
    private String additionalInfo;

    @Column(name = "PHONE", nullable = false)
    private String phone;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;
}
