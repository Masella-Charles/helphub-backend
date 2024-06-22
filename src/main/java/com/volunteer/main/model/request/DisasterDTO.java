package com.volunteer.main.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DisasterDTO {
    private Long Id;
    private String name;
    private String description;
    private LocalDate date;
    private Boolean status;
}
