package com.volunteer.main.service;

import com.volunteer.main.model.request.OpportunityImageDTO;
import org.springframework.stereotype.Service;

@Service
public interface OpportunityImageService {
    OpportunityImageDTO createImage(OpportunityImageDTO opportunityImageDTO);
    OpportunityImageDTO updateImage(OpportunityImageDTO opportunityImageDTO);
    OpportunityImageDTO getImageByOpportunityId(Long opportunityId);
    void deleteImage(Long id);
}
