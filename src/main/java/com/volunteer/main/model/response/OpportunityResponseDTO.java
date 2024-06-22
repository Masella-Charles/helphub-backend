package com.volunteer.main.model.response;


import com.volunteer.main.model.request.OpportunityDTO;
import lombok.Data;

@Data
public class OpportunityResponseDTO {
    private ResponseStatus responseStatus;
    private OpportunityDTO opportunityDTO;
}
