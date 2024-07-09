package com.volunteer.main.model.response;


import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.model.request.OpportunityImageDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OpportunityResponseDTO {
    private ResponseStatus responseStatus;
    private OpportunityResponse opportunityResponse;
    @Data
    public static class OpportunityResponse{
        private Long id;
        private String name;
        private String description;
        private LocalDate date;
        private Integer requiredVolunteers;
        private Double hours;
        private Boolean status;
        private List<OpportunityImageResponseDTO> opportunityImages;
        private Long disasterId;
    }
}
