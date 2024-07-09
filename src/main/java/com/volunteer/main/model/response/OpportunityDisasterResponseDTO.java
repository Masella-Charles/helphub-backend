package com.volunteer.main.model.response;

import com.volunteer.main.model.request.OpportunityImageDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OpportunityDisasterResponseDTO {
    private ResponseStatus responseStatus;
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Integer requiredVolunteers;
    private Double hours;
    private Boolean status;
    private List<OpportunityImageDTO> opportunityImages;
    private DisasterEntity disasterEntity; // Reference to DisasterEntity

    @Data
    public static class DisasterEntity{
        private Long tId;
        private String name;
        private String description;
        private LocalDate date;
        private Boolean status;
    }
}
