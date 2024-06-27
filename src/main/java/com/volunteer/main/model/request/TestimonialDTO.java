package com.volunteer.main.model.request;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TestimonialDTO {
    private Long id;
    private String testimonial;
    private Boolean status;
    private LocalDate createdAt;
    private Long userId; // Reference to UserEntity
}
