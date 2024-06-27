package com.volunteer.main.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volunteer.main.entity.*;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.model.request.OpportunityUserDTO;
import com.volunteer.main.model.response.*;
import com.volunteer.main.repositories.DisasterRepository;
import com.volunteer.main.repositories.OpportunityRepository;
import com.volunteer.main.repositories.OpportunityUserRepository;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.OpportunityService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OpportunityServiceImpl implements OpportunityService {
    private static final Logger logger = LoggerFactory.getLogger(DonationServiceImpl.class);
    private final OpportunityRepository opportunityRepository;
    private final UserRepository userRepository;
    private final DisasterRepository disasterRepository;
    private final Utils utils;
    private final OpportunityUserRepository opportunityUserRepository;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository, UserRepository userRepository, DisasterRepository disasterRepository, Utils utils, OpportunityUserRepository opportunityUserRepository) {
        this.opportunityRepository = opportunityRepository;
        this.userRepository = userRepository;
        this.disasterRepository = disasterRepository;
        this.utils = utils;
        this.opportunityUserRepository = opportunityUserRepository;
    }

    @Override
    public ResponseEntity<?> createOpportunity(OpportunityDTO opportunityDTO, MultipartFile opportunityImage) {
        try {
            OpportunityEntity opportunityEntity = new OpportunityEntity();
            opportunityEntity.setName(opportunityDTO.getName());
            opportunityEntity.setDescription(opportunityDTO.getDescription());
            opportunityEntity.setDate(LocalDate.now());
            opportunityEntity.setHours(opportunityDTO.getHours());
            opportunityEntity.setRequiredVolunteers(opportunityDTO.getRequiredVolunteers());
            opportunityEntity.setStatus(false);

            // Convert MultipartFile to byte[] and set it
            if (opportunityImage != null && !opportunityImage.isEmpty()) {
                opportunityEntity.setOpportunityImage(opportunityImage.getBytes());
            }

            // Handle optional disasterId
            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            // Create response DTO
            OpportunityDTO opportunitySaved = new OpportunityDTO();
            opportunitySaved.setId(opportunityEntity.getId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());
            opportunitySaved.setOpportunityImage(opportunityEntity.getOpportunityImage());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getId());
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
    public ResponseEntity<?> updateOpportunity(OpportunityDTO opportunityDTO, MultipartFile opportunityImage) {
        try {
            OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Opportunity not found with id: " + opportunityDTO.getId(), new Throwable()));

            opportunityEntity.setId(opportunityDTO.getId());
            opportunityEntity.setName(opportunityDTO.getName());
            opportunityEntity.setDescription(opportunityDTO.getDescription());
            opportunityEntity.setHours(opportunityDTO.getHours());
            opportunityEntity.setRequiredVolunteers(opportunityDTO.getRequiredVolunteers());

            // Convert MultipartFile to byte[] and set it
            if (opportunityImage != null && !opportunityImage.isEmpty()) {
                opportunityEntity.setOpportunityImage(opportunityImage.getBytes());
            }

            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            OpportunityDTO opportunitySaved = new OpportunityDTO();
            opportunitySaved.setId(opportunityEntity.getId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());
            opportunitySaved.setOpportunityImage(opportunityEntity.getOpportunityImage());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getId());
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

            opportunityEntity.setId(opportunityDTO.getId());
            opportunityEntity.setStatus(opportunityDTO.getStatus());

            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            OpportunityDTO opportunitySaved = new OpportunityDTO();
            opportunitySaved.setId(opportunityEntity.getId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());
            opportunitySaved.setOpportunityImage(opportunityEntity.getOpportunityImage());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getId());
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

//    @Override
    public ResponseEntity<?> getOpportunityById(OpportunityDTO opportunityDTO) {
        try {
            OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Donation not found with id: " + opportunityDTO.getId(), new Throwable()));


            logger.info("opportunityEntity: {}", opportunityEntity);
            opportunityEntity.setId(opportunityDTO.getId());


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(opportunityEntity);

            OpportunityDisasterResponseDTO opportunityDisasterResponseDTO = (OpportunityDisasterResponseDTO) utils.setJsonStringToObject(json.toString(), OpportunityDisasterResponseDTO.class);
            opportunityDisasterResponseDTO.setId(opportunityEntity.getId());
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

    @Override
    public List<OpportunityDisasterResponseDTO> getOpportunitiesByIdStatusOrDisasterId(Long id, Boolean status, Long disasterId) {
        try {
            List<OpportunityEntity> opportunityEntities;
            if (id != null) {
                Optional<OpportunityEntity> opportunityEntityOptional = opportunityRepository.findById(id);
                opportunityEntities = opportunityEntityOptional.map(Collections::singletonList).orElse(Collections.emptyList());
            } else if (status != null) {
                opportunityEntities = opportunityRepository.findByStatus(status);
            } else if (disasterId != null) {
                opportunityEntities = opportunityRepository.findByDisasterEntityId(disasterId);
            } else {
                throw new IllegalArgumentException("Either id, status, or disasterId must be provided");
            }

            return opportunityEntities.stream().map(this::mapToOpportunityDisasterResponseDTO).collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while fetching opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private OpportunityDisasterResponseDTO mapToOpportunityDisasterResponseDTO(OpportunityEntity opportunityEntity) {
        OpportunityDisasterResponseDTO dto = new OpportunityDisasterResponseDTO();
        dto.setId(opportunityEntity.getId());
        dto.setName(opportunityEntity.getName());
        dto.setDescription(opportunityEntity.getDescription());
        dto.setDate(opportunityEntity.getDate());
        dto.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
        dto.setHours(opportunityEntity.getHours());
        dto.setStatus(opportunityEntity.getStatus());
        dto.setOpportunityImage(opportunityEntity.getOpportunityImage());

        OpportunityDisasterResponseDTO.DisasterEntity disasterEntity = new OpportunityDisasterResponseDTO.DisasterEntity();
        disasterEntity.setTId(opportunityEntity.getDisasterEntity().getId());
        disasterEntity.setName(opportunityEntity.getDisasterEntity().getName());
        disasterEntity.setDescription(opportunityEntity.getDisasterEntity().getDescription());
        disasterEntity.setDate(opportunityEntity.getDisasterEntity().getDate());
        disasterEntity.setStatus(opportunityEntity.getDisasterEntity().getStatus());
        dto.setDisasterEntity(disasterEntity);

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseCode("200");
        responseStatus.setResponseDesc("Fetched successfully");

        dto.setResponseStatus(responseStatus);

        return dto;
    }

    @Override
    public OpportunityUserDTO volunteerNow(Long userId, Long opportunityId) {
        try {
            UserEntity user = userRepository.findById(Math.toIntExact(userId))
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            OpportunityEntity opportunity = opportunityRepository.findById(opportunityId)
                    .orElseThrow(() -> new EntityNotFoundException("Opportunity not found with id: " + opportunityId));

            // Check if the opportunity already has the required number of volunteers
            long currentVolunteerCount = opportunityRepository.countByRequiredVolunteers(opportunity);
            if (currentVolunteerCount >= opportunity.getRequiredVolunteers()) {
                throw new CustomAuthenticationException("The opportunity has already reached the required number of volunteers.",null);
            }

            // Check if the user has at least one skill
            UserEntity userEntity = userRepository.findById(Math.toIntExact(userId))
                    .orElseThrow(() -> new EntityNotFoundException("Volunteer not found for user with id: " + userId));

            if (userEntity.getVolunteer().getSkills() == null || userEntity.getVolunteer().getSkills().isEmpty()) {
                throw new CustomAuthenticationException("User must have at least one skill to volunteer.", null);
            }

            OpportunityUserEntity opportunityUserEntity = new OpportunityUserEntity();
            opportunityUserEntity.setUser(user);
            opportunityUserEntity.setOpportunity(opportunity);
            opportunityUserEntity.setStatus(false);
            opportunityUserEntity = opportunityUserRepository.save(opportunityUserEntity);

            OpportunityUserDTO opportunityUserDTO = new OpportunityUserDTO();
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer matched successfully");
            opportunityUserDTO.setResponseStatus(responseStatus);
            opportunityUserDTO.setUserId(user.getId());
            opportunityUserDTO.setUserName(user.getFullName());
            opportunityUserDTO.setUserEmail(user.getEmail());
            opportunityUserDTO.setUserSkills((List<String>) user.getVolunteer().getSkills());
            opportunityUserDTO.setOpportunityId(opportunity.getId());
            opportunityUserDTO.setOpportunityName(opportunity.getName());
            opportunityUserDTO.setOpportunityDescription(opportunity.getDescription());
            opportunityUserDTO.setStatus(opportunityUserEntity.getStatus());

            return opportunityUserDTO;
        }catch (DataAccessException e) {
            logger.error("Database error while volunteering: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while volunteering: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public OpportunityUserDTO volunteerTransition(Long userId, Long opportunityId, Boolean status) {
        try {
            UserEntity user = userRepository.findById(Math.toIntExact(userId))
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            OpportunityEntity opportunity = opportunityRepository.findById(opportunityId)
                    .orElseThrow(() -> new EntityNotFoundException("Opportunity not found with id: " + opportunityId));

            // Check if the opportunity already has the required number of volunteers
            long currentVolunteerCount = opportunityRepository.countByRequiredVolunteers(opportunity);
            if (currentVolunteerCount >= opportunity.getRequiredVolunteers()) {
                throw new CustomAuthenticationException("The opportunity has already reached the required number of volunteers.", null);
            }

            OpportunityUserEntity opportunityUserEntity = new OpportunityUserEntity();
            opportunityUserEntity.setUser(user);
            opportunityUserEntity.setOpportunity(opportunity);
            opportunityUserEntity.setStatus(status);
            opportunityUserEntity = opportunityUserRepository.save(opportunityUserEntity);

            OpportunityUserDTO opportunityUserDTO = new OpportunityUserDTO();
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer transitioned successfully");
            opportunityUserDTO.setResponseStatus(responseStatus);
            opportunityUserDTO.setUserId(user.getId());
            opportunityUserDTO.setUserName(user.getFullName());
            opportunityUserDTO.setUserEmail(user.getEmail());
            opportunityUserDTO.setUserSkills((List<String>) user.getVolunteer().getSkills());
            opportunityUserDTO.setOpportunityId(opportunity.getId());
            opportunityUserDTO.setOpportunityName(opportunity.getName());
            opportunityUserDTO.setOpportunityDescription(opportunity.getDescription());
            opportunityUserDTO.setStatus(opportunityUserEntity.getStatus());

            return opportunityUserDTO;
        }catch (DataAccessException e) {
            logger.error("Database error while transitioning: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while transitioning: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    public Object  getOpportunityUserByIdOrStatusOrUserIdOrOpportunityId(Long id, Boolean status, Long userId, Long opportunityId) {
        try {
            if (id != null) {
                Optional<OpportunityUserEntity> opportunityUserEntityOptional = opportunityUserRepository.findById(Math.toIntExact(id));
                OpportunityUserEntity opportunityUserEntity = opportunityUserEntityOptional
                        .orElseThrow(() -> new EntityNotFoundException("OpportunityUserEntity not found with id: " + id));
                return mapToDTO(opportunityUserEntity);
            } else if (status != null) {
                List<OpportunityUserEntity> opportunityUserEntities = opportunityUserRepository.findByStatus(status);
                return opportunityUserEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
            } else if (userId != null) {
                List<OpportunityUserEntity> opportunityUserEntities = opportunityUserRepository.findByUserId(userId);
                return opportunityUserEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
            } else if (opportunityId != null) {
                List<OpportunityUserEntity> opportunityUserEntities = opportunityUserRepository.findByOpportunityId(opportunityId);
                return opportunityUserEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
            } else {
                throw new IllegalArgumentException("Either id, status, userId, or opportunityId must be provided");
            }
        } catch (DataAccessException e) {
            logger.error("Database error while fetching opportunity user: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching opportunity user: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OpportunityUserDTO> getAllOpportunityUsers() {
        try {
            List<OpportunityUserEntity> opportunityUserEntities = opportunityUserRepository.findAll();
            return opportunityUserEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while fetching all opportunity users: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all opportunity users: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private OpportunityUserDTO mapToDTO(OpportunityUserEntity opportunityUserEntity) {
        OpportunityUserDTO opportunityUserDTO = new OpportunityUserDTO();
        opportunityUserDTO.setUserId(opportunityUserEntity.getUser().getId());
        opportunityUserDTO.setUserName(opportunityUserEntity.getUser().getFullName());
        opportunityUserDTO.setUserEmail(opportunityUserEntity.getUser().getEmail());
        opportunityUserDTO.setUserSkills((List<String>) opportunityUserEntity.getUser().getVolunteer().getSkills());
        opportunityUserDTO.setOpportunityId(opportunityUserEntity.getOpportunity().getId());
        opportunityUserDTO.setOpportunityName(opportunityUserEntity.getOpportunity().getName());
        opportunityUserDTO.setOpportunityDescription(opportunityUserEntity.getOpportunity().getDescription());
        opportunityUserDTO.setStatus(opportunityUserEntity.getStatus());

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseCode("200");
        responseStatus.setResponseDesc("Fetched successfully");

        opportunityUserDTO.setResponseStatus(responseStatus);

        return opportunityUserDTO;
    }
}
