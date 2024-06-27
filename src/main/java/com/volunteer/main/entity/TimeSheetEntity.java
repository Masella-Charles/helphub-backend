package com.volunteer.main.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TIMESHEET_TABLE")
@Data
public class TimeSheetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIMESHEET_SEQ")
    @SequenceGenerator(name = "TIMESHEET_SEQ", sequenceName = "TIMESHEET_SEQ", allocationSize = 1)
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

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
