package com.volunteer.main.model.response;

import com.volunteer.main.model.request.OpportunityUserDTO;
import lombok.Data;

@Data
public class OpportunityUserResponseDTO {
    private ResponseStatus responseStatus;
    private OpportunityUserDTO opportunityUser;
}
