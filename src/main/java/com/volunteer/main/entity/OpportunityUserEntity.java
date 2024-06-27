package com.volunteer.main.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Entity
@Table(name = "OPPORTUNITY-USER-TABLE")
@Data
public class OpportunityUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPPORTUNITY-USER_SEQ")
    @SequenceGenerator(name = "OPPORTUNITY-USER_SEQ", sequenceName = "OPPORTUNITY-USER_SEQ", allocationSize = 1)
    @Column(name = "T_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "T_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "opportunity_id", referencedColumnName = "T_ID")
    private OpportunityEntity opportunity;

    @Column(name = "status")
    private Boolean status;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
