package com.volunteer.main.model.request;


import com.volunteer.main.model.response.ResponseStatus;
import lombok.Data;

import java.util.List;

@Data
public class OpportunityUserDTO {
    private ResponseStatus responseStatus;
    private Long userId;
    private String userName;
    private String userEmail;
    private List<String> userSkills;
    private Long opportunityId;
    private String opportunityName;
    private String opportunityDescription;
    private Boolean status;
}
