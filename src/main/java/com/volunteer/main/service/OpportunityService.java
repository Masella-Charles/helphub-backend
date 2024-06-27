package com.volunteer.main.service;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.model.request.OpportunityUserDTO;
import com.volunteer.main.model.response.OpportunityDisasterResponseDTO;
import com.volunteer.main.model.response.VolunteerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface OpportunityService {
    ResponseEntity<?> createOpportunity(OpportunityDTO opportunityDTO, MultipartFile opportunityImage);
    ResponseEntity<?> updateOpportunity(OpportunityDTO opportunityDTO, MultipartFile opportunityImage);
    ResponseEntity<?> transitionOpportunity(OpportunityDTO opportunityDTO);
    List<OpportunityDisasterResponseDTO> getOpportunitiesByIdStatusOrDisasterId(Long id, Boolean status, Long disasterId);
    List<OpportunityEntity> getAllOpportunities();

    OpportunityUserDTO volunteerNow(Long userId, Long opportunityId);
    OpportunityUserDTO volunteerTransition(Long userId, Long opportunityId, Boolean status);
    Object  getOpportunityUserByIdOrStatusOrUserIdOrOpportunityId(Long id, Boolean status, Long userId, Long opportunityId);
    List<OpportunityUserDTO> getAllOpportunityUsers();


}
