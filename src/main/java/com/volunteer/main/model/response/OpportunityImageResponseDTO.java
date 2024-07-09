package com.volunteer.main.model.response;


import lombok.Data;

import java.util.List;

@Data
public class OpportunityImageResponseDTO {
    private Long id;
    private String fileName;
    private String imageData;
    private Long opportunityId;
}
