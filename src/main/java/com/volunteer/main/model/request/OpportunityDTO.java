package com.volunteer.main.model.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class OpportunityDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Integer requiredVolunteers;
    private Double hours;
    private Boolean status;
    private List<OpportunityImageDTO> opportunityImages;
    private Long disasterId;
}
