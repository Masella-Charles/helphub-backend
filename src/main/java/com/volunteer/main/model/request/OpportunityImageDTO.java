package com.volunteer.main.model.request;

import lombok.Data;

@Data
public class OpportunityImageDTO {
    private Long id;
    private String fileName;
    private String  imageData;
    private Long opportunityId;
}
