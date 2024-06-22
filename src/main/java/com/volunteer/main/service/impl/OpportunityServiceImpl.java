package com.volunteer.main.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.model.response.*;
import com.volunteer.main.repositories.DisasterRepository;
import com.volunteer.main.repositories.OpportunityRepository;
import com.volunteer.main.service.OpportunityService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OpportunityServiceImpl implements OpportunityService {
    private static final Logger logger = LoggerFactory.getLogger(DonationServiceImpl.class);
    private final OpportunityRepository opportunityRepository;
    private final DisasterRepository disasterRepository;
    private final Utils utils;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository, DisasterRepository disasterRepository, Utils utils) {
        this.opportunityRepository = opportunityRepository;
        this.disasterRepository = disasterRepository;
        this.utils = utils;
    }

    @Override
    public ResponseEntity<?> createOpportunity(OpportunityDTO opportunityDTO) {
        try {
            OpportunityEntity opportunityEntity = new OpportunityEntity();
            opportunityEntity.setName(opportunityDTO.getName());
            opportunityEntity.setDescription(opportunityDTO.getDescription());
            opportunityEntity.setDate(utils.date());
            opportunityEntity.setHours(opportunityDTO.getHours());
            opportunityEntity.setRequiredVolunteers(opportunityDTO.getRequiredVolunteers());
            opportunityEntity.setStatus(false);

            // Handle optional disasterId
            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            // Create response DTO
            OpportunityDTO opportunitySaved = new OpportunityDTO();
            opportunitySaved.setId(opportunityEntity.getTId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getTId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Opportunity created successfully");

            OpportunityResponseDTO opportunityResponseDTO = new OpportunityResponseDTO();
            opportunityResponseDTO.setOpportunityDTO(opportunitySaved);
            opportunityResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(opportunityResponseDTO);
        } catch (DataAccessException e) {
            logger.error("Database error while creating opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> updateOpportunity(OpportunityDTO opportunityDTO) {
        try {
            OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Opportunity not found with id: " + opportunityDTO.getId(), new Throwable()));

            opportunityEntity.setTId(opportunityDTO.getId());
            opportunityEntity.setName(opportunityDTO.getName());
            opportunityEntity.setDescription(opportunityDTO.getDescription());
            opportunityEntity.setHours(opportunityDTO.getHours());
            opportunityEntity.setRequiredVolunteers(opportunityDTO.getRequiredVolunteers());

            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            OpportunityDTO opportunitySaved = new OpportunityDTO();
            opportunitySaved.setId(opportunityEntity.getTId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getTId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation updated successfully");

            OpportunityResponseDTO opportunityResponseDTO = new OpportunityResponseDTO();
            opportunityResponseDTO.setOpportunityDTO(opportunitySaved);
            opportunityResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(opportunityResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while updating opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while updating opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> transitionOpportunity(OpportunityDTO opportunityDTO) {
        try {
            OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Opportunity not found with id: " + opportunityDTO.getId(), new Throwable()));

            opportunityEntity.setTId(opportunityDTO.getId());
            opportunityEntity.setStatus(opportunityDTO.getStatus());

            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            OpportunityDTO opportunitySaved = new OpportunityDTO();
            opportunitySaved.setId(opportunityEntity.getTId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getTId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Opportunity transitioned successfully");

            OpportunityResponseDTO opportunityResponseDTO = new OpportunityResponseDTO();
            opportunityResponseDTO.setOpportunityDTO(opportunitySaved);
            opportunityResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(opportunityResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while transitioning opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while transitioning opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getOpportunityById(OpportunityDTO opportunityDTO) {
        try {
            OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Donation not found with id: " + opportunityDTO.getId(), new Throwable()));


            logger.info("opportunityEntity: {}", opportunityEntity);
            opportunityEntity.setTId(opportunityDTO.getId());


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(opportunityEntity);

            OpportunityDisasterResponseDTO opportunityDisasterResponseDTO = (OpportunityDisasterResponseDTO) utils.setJsonStringToObject(json.toString(), OpportunityDisasterResponseDTO.class);
            opportunityDisasterResponseDTO.setId(opportunityEntity.getTId());
            logger.info("opportunityDisasterResponseDTO: {}", opportunityDisasterResponseDTO);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Opportunity fetched successfully");

            opportunityDisasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(opportunityDisasterResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while fetching opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching opportunity: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OpportunityEntity> getAllOpportunities() {
        try {
            Iterable<OpportunityEntity> opportunityEntityIterable = opportunityRepository.findAll();
            return StreamSupport.stream(opportunityEntityIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while listing all opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }
}
