package com.volunteer.main.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="contact_us")
@Data
public class ContactUsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACTUS_SEQ")
    @SequenceGenerator(name = "CONTACTUS_SEQ", sequenceName = "CONTACTUS_SEQ", allocationSize = 1)
    @Column(name = "T_ID")
    private Long id;

    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
    private String stage;
}
