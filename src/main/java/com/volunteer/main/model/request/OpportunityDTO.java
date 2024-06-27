package com.volunteer.main.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OpportunityDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Integer requiredVolunteers;
    private Double hours;
    private Boolean status;
    private byte[] opportunityImage;
    private Long disasterId;
}
