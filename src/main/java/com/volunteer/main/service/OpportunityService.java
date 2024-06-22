package com.volunteer.main.service;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.OpportunityDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpportunityService {
    ResponseEntity<?> createOpportunity(OpportunityDTO opportunityDTO);
    ResponseEntity<?> updateOpportunity(OpportunityDTO opportunityDTO);
    ResponseEntity<?> transitionOpportunity(OpportunityDTO opportunityDTO);
    ResponseEntity<?> getOpportunityById(OpportunityDTO opportunityDTO);
    List<OpportunityEntity> getAllOpportunities();
}
