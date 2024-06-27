package com.volunteer.main.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="testimonials")
@Data
public class TestimonialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TESTIMONIALS_SEQ")
    @SequenceGenerator(name = "TESTIMONIALS_SEQ", sequenceName = "TESTIMONIALS_SEQ", allocationSize = 1)
    @Column(name = "T_ID")
    private Long id;

    @Column(name = "TESTIMONIAL", unique = true, nullable = false)
    private String testimonial;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
