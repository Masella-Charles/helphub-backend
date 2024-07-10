package com.volunteer.main.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volunteer.main.entity.*;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.model.request.OpportunityImageDTO;
import com.volunteer.main.model.request.OpportunityUserDTO;
import com.volunteer.main.model.response.*;
import com.volunteer.main.repositories.*;
import com.volunteer.main.service.OpportunityImageService;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpportunityServiceImpl implements OpportunityService {
    private static final Logger logger = LoggerFactory.getLogger(OpportunityServiceImpl.class);
    private final OpportunityRepository opportunityRepository;
    private final UserRepository userRepository;
    private final DisasterRepository disasterRepository;
    private final Utils utils;
    private final OpportunityUserRepository opportunityUserRepository;
    private final OpportunityImageService opportunityImageService;
    private final OpportunityImageRepository opportunityImageRepository;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository, UserRepository userRepository, DisasterRepository disasterRepository, Utils utils, OpportunityUserRepository opportunityUserRepository, OpportunityImageService opportunityImageService, OpportunityImageRepository opportunityImageRepository) {
        this.opportunityRepository = opportunityRepository;
        this.userRepository = userRepository;
        this.disasterRepository = disasterRepository;
        this.utils = utils;
        this.opportunityUserRepository = opportunityUserRepository;
        this.opportunityImageService = opportunityImageService;
        this.opportunityImageRepository = opportunityImageRepository;
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

            // Handle optional disasterId
            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            List<OpportunityImageEntity> opportunityImages = new ArrayList<>();

            // Handle image creation
            for (OpportunityImageDTO imageDTO : opportunityDTO.getOpportunityImages()) {
                if (imageDTO.getImageData() != null && !imageDTO.getImageData().isEmpty()) {
                    String base64ImageData = imageDTO.getImageData();
                    // Remove the prefix if it exists
                    if (base64ImageData.contains(",")) {
                        base64ImageData = base64ImageData.split(",")[1];
                    }
                    byte[] imageData = Base64.getDecoder().decode(base64ImageData);

                    OpportunityImageEntity opportunityImageEntity = new OpportunityImageEntity();
                    opportunityImageEntity.setOpportunityEntity(opportunityEntity);
                    opportunityImageEntity.setImageData(imageData);
                    opportunityImageEntity.setFileName(imageDTO.getFileName());

                    opportunityImages.add(opportunityImageEntity);
                }
            }

            opportunityEntity.setOpportunityImages(opportunityImages);

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            // Create response DTO
            OpportunityResponseDTO.OpportunityResponse opportunitySaved = new OpportunityResponseDTO.OpportunityResponse();
            opportunitySaved.setId(opportunityEntity.getId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());

            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Opportunity created successfully");

            OpportunityResponseDTO opportunityResponseDTO = new OpportunityResponseDTO();
            opportunityResponseDTO.setOpportunityResponse(opportunitySaved);
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



            if (opportunityDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(opportunityDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + opportunityDTO.getDisasterId()));
                opportunityEntity.setDisasterEntity(disaster);
            }

            List<OpportunityImageEntity> opportunityImages = new ArrayList<>();

            // Handle image creation
            for (OpportunityImageDTO imageDTO : opportunityDTO.getOpportunityImages()) {
                if (imageDTO.getImageData() != null && !imageDTO.getImageData().isEmpty()) {
                    String base64ImageData = imageDTO.getImageData();
                    // Remove the prefix if it exists
                    if (base64ImageData.contains(",")) {
                        base64ImageData = base64ImageData.split(",")[1];
                    }
                    byte[] imageData = Base64.getDecoder().decode(base64ImageData);

                    OpportunityImageEntity opportunityImageEntity = new OpportunityImageEntity();
                    opportunityImageEntity.setOpportunityEntity(opportunityEntity);
                    opportunityImageEntity.setImageData(imageData);
                    opportunityImageEntity.setFileName(imageDTO.getFileName());

                    opportunityImages.add(opportunityImageEntity);
                }
            }

            opportunityEntity.setOpportunityImages(opportunityImages);

            opportunityEntity = opportunityRepository.save(opportunityEntity);

            OpportunityResponseDTO.OpportunityResponse opportunitySaved = new OpportunityResponseDTO.OpportunityResponse();
            opportunitySaved.setId(opportunityEntity.getId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());
//            opportunitySaved.setOpportunityImages(opportunityImages);

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation updated successfully");

            OpportunityResponseDTO opportunityResponseDTO = new OpportunityResponseDTO();
            opportunityResponseDTO.setOpportunityResponse(opportunitySaved);
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

            OpportunityResponseDTO.OpportunityResponse opportunitySaved = new OpportunityResponseDTO.OpportunityResponse();
            opportunitySaved.setId(opportunityEntity.getId());
            opportunitySaved.setName(opportunityEntity.getName());
            opportunitySaved.setDescription(opportunityEntity.getDescription());
            opportunitySaved.setDate(opportunityEntity.getDate());
            opportunitySaved.setHours(opportunityEntity.getHours());
            opportunitySaved.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
            opportunitySaved.setStatus(opportunityEntity.getStatus());
//            opportunitySaved.setOpportunityImage(opportunityEntity.getOpportunityImage());

            // Only set disasterId if the disaster is not null
            if (opportunityEntity.getDisasterEntity() != null) {
                opportunitySaved.setDisasterId(opportunityEntity.getDisasterEntity().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Opportunity transitioned successfully");

            OpportunityResponseDTO opportunityResponseDTO = new OpportunityResponseDTO();
            opportunityResponseDTO.setOpportunityResponse(opportunitySaved);
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
    public List<OpportunityResponseDTO> getAllOpportunities() {
        try {
            Iterable<OpportunityEntity> opportunityEntityIterable = opportunityRepository.findAll();
            List<OpportunityResponseDTO> responseList = StreamSupport.stream(opportunityEntityIterable.spliterator(), false)
                    .map(this::mapToOpportunityListDTO)
                    .collect(Collectors.toList());

            responseList.forEach(responseDTO -> {
                OpportunityResponseDTO.OpportunityResponse opportunityResponse = responseDTO.getOpportunityResponse();
                OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityResponse.getId()).orElse(null);
                if (opportunityEntity != null) {
                    List<OpportunityImageResponseDTO> imageInfoList = opportunityEntity.getOpportunityImages().stream()
                            .map(image -> {
                                OpportunityImageResponseDTO imageInfoDTO = new OpportunityImageResponseDTO();
                                imageInfoDTO.setId(image.getId());
                                imageInfoDTO.setFileName(image.getFileName());
                                imageInfoDTO.setOpportunityId(image.getOpportunityEntity().getId());
                                imageInfoDTO.setImageData(Base64.getEncoder().encodeToString(image.getImageData())); // Encode image data to Base64
                                return imageInfoDTO;
                            })
                            .collect(Collectors.toList());
                    opportunityResponse.setOpportunityImages(imageInfoList);
                }
            });

            return responseList;
        } catch (DataAccessException e) {
            logger.error("Database error while listing all opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private OpportunityResponseDTO mapToOpportunityListDTO(OpportunityEntity opportunityEntity) {
        OpportunityResponseDTO dto = new OpportunityResponseDTO();
        OpportunityResponseDTO.OpportunityResponse opportunityResponse = new OpportunityResponseDTO.OpportunityResponse();
        opportunityResponse.setId(opportunityEntity.getId());
        opportunityResponse.setName(opportunityEntity.getName());
        opportunityResponse.setDescription(opportunityEntity.getDescription());
        opportunityResponse.setDate(opportunityEntity.getDate());
        opportunityResponse.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
        opportunityResponse.setHours(opportunityEntity.getHours());
        opportunityResponse.setStatus(opportunityEntity.getStatus());

        if (opportunityEntity.getDisasterEntity() != null) {
            opportunityResponse.setDisasterId(opportunityEntity.getDisasterEntity().getId());
        }

        List<OpportunityImageResponseDTO> imageInfoList = opportunityEntity.getOpportunityImages().stream()
                .map(image -> {
                    OpportunityImageResponseDTO imageInfoDTO = new OpportunityImageResponseDTO();
                    imageInfoDTO.setId(image.getId());
                    imageInfoDTO.setFileName(image.getFileName());
                    imageInfoDTO.setOpportunityId(image.getOpportunityEntity().getId());
                    imageInfoDTO.setImageData(Base64.getEncoder().encodeToString(image.getImageData())); // Encode image data to Base64
                    return imageInfoDTO;
                })
                .collect(Collectors.toList());
        opportunityResponse.setOpportunityImages(imageInfoList);

        dto.setOpportunityResponse(opportunityResponse);

        return dto;
    }

    @Override
    public List<OpportunityResponseDTO> getOpportunitiesByCriteria(Long opportunityId, Boolean status) {
        try {
            List<OpportunityEntity> opportunityEntities = new ArrayList<>();;

            if (opportunityId != null && status != null) {
                opportunityEntities = opportunityRepository.findByIdAndStatus(opportunityId, status);
            } else if (opportunityId != null) {
                Optional<OpportunityEntity> opportunityEntitiesOptional= opportunityRepository.findById(opportunityId);
                logger.info("opportunityEntitiesOptional {}", opportunityEntitiesOptional);
                if (opportunityEntitiesOptional.isPresent()) {
                    OpportunityEntity opportunityEntity = opportunityEntitiesOptional.get();
                    logger.info("opportunityEntity {}", opportunityEntity);
                    opportunityEntities.add(opportunityEntity);
                    logger.info("opportunityEntities {}", opportunityEntities);

                }else{
                    opportunityEntities = Collections.emptyList();
                }
            } else if (status != null) {
                opportunityEntities = opportunityRepository.findByStatus(status);
                logger.info("opportunityEntities {}", opportunityEntities);
            } else {
                // Case: no criteria provided, fetch all
                opportunityEntities = opportunityRepository.findAll();
            }

            return opportunityEntities.stream()
                    .map(this::mapToOpportunityListDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while fetching opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OpportunityResponseDTO> getOpportunitiesByStatus(Boolean status) {
        try {
            List<OpportunityEntity> opportunityEntities = opportunityRepository.findByStatus(status);

            List<OpportunityResponseDTO> responseList = opportunityEntities.stream()
                    .map(this::mapToOpportunityDTOForGetBy)
                    .collect(Collectors.toList());

            responseList.forEach(responseDTO -> {
                Long opportunityId = responseDTO.getOpportunityResponse().getId();
                Optional<OpportunityImageEntity> imageEntities = opportunityImageRepository.findByOpportunityEntity_Id(opportunityId);

                List<OpportunityImageResponseDTO> imageInfoList = imageEntities.stream()
                        .map(image -> {
                            OpportunityImageResponseDTO imageInfoDTO = new OpportunityImageResponseDTO();
                            imageInfoDTO.setId(image.getId());
                            imageInfoDTO.setFileName(image.getFileName());
                            imageInfoDTO.setOpportunityId(opportunityId);
                            imageInfoDTO.setImageData(Base64.getEncoder().encodeToString(image.getImageData())); // Encode image data to Base64
                            return imageInfoDTO;
                        })
                        .collect(Collectors.toList());

                responseDTO.getOpportunityResponse().setOpportunityImages(imageInfoList);
            });

            return responseList;
        } catch (DataAccessException e) {
            logger.error("Database error while fetching opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching opportunities: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }
    private OpportunityResponseDTO mapToOpportunityDTOForGetBy(OpportunityEntity opportunityEntity) {
        OpportunityResponseDTO dto = new OpportunityResponseDTO();
        OpportunityResponseDTO.OpportunityResponse opportunityResponse = new OpportunityResponseDTO.OpportunityResponse();
        opportunityResponse.setId(opportunityEntity.getId());
        opportunityResponse.setName(opportunityEntity.getName());
        opportunityResponse.setDescription(opportunityEntity.getDescription());
        opportunityResponse.setDate(opportunityEntity.getDate());
        opportunityResponse.setRequiredVolunteers(opportunityEntity.getRequiredVolunteers());
        opportunityResponse.setHours(opportunityEntity.getHours());
        opportunityResponse.setStatus(opportunityEntity.getStatus());

        if (opportunityEntity.getDisasterEntity() != null) {
            opportunityResponse.setDisasterId(opportunityEntity.getDisasterEntity().getId());
        }
        dto.setOpportunityResponse(opportunityResponse);

        return dto;
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

        // Map opportunity images
        List<OpportunityImageDTO> opportunityImageDTOs = opportunityEntity.getOpportunityImages().stream()
                .map(imageEntity -> {
                    OpportunityImageDTO imageDTO = new OpportunityImageDTO();
                    imageDTO.setId(imageEntity.getId());
                    imageDTO.setFileName(imageEntity.getFileName());
                    imageDTO.setImageData(Arrays.toString(imageEntity.getImageData()));
                    imageDTO.setOpportunityId(opportunityEntity.getId());
                    return imageDTO;
                })
                .collect(Collectors.toList());
        dto.setOpportunityImages(opportunityImageDTOs);

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
            long currentVolunteerCount = opportunityUserRepository.countByOpportunityId(opportunityId);
            if (currentVolunteerCount >= opportunity.getRequiredVolunteers()) {
                throw new CustomAuthenticationException("The opportunity has already reached the required number of volunteers.", null);
            }

            // Check if the user has at least one skill
            if (user.getVolunteer().getSkills() == null || user.getVolunteer().getSkills().isEmpty()) {
                throw new CustomAuthenticationException("User must have at least one skill to volunteer.", null);
            }

            OpportunityUserEntity opportunityUserEntity = new OpportunityUserEntity();
            opportunityUserEntity.setUser(user);
            opportunityUserEntity.setOpportunity(opportunity);
            opportunityUserEntity.setStatus(false);
            opportunityUserEntity.setCreatedAt(utils.date());
            opportunityUserEntity.setUpdatedAt(null);
            opportunityUserEntity = opportunityUserRepository.save(opportunityUserEntity);

            OpportunityUserDTO opportunityUserDTO = new OpportunityUserDTO();
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer matched successfully");
            opportunityUserDTO.setResponseStatus(responseStatus);
            opportunityUserDTO.setUserId(user.getId());
            opportunityUserDTO.setUserName(user.getFullName());
            opportunityUserDTO.setUserEmail(user.getEmail());

            // Convert Set to List
            List<String> userSkills = new ArrayList<>(user.getVolunteer().getSkills());
            opportunityUserDTO.setUserSkills(userSkills);

            opportunityUserDTO.setOpportunityId(opportunity.getId());
            opportunityUserDTO.setOpportunityName(opportunity.getName());
            opportunityUserDTO.setOpportunityDescription(opportunity.getDescription());

            opportunityUserDTO.setId(opportunityUserEntity.getId());
            opportunityUserDTO.setStatus(opportunityUserEntity.getStatus());

            return opportunityUserDTO;
        } catch (DataAccessException e) {
            logger.error("Database error while volunteering: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while volunteering: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public OpportunityUserDTO volunteerTransition(OpportunityUserDTO opportunityUserDTO1) {
        try {
            OpportunityUserEntity opportunityUserEntity = opportunityUserRepository.findById(opportunityUserDTO1.getId())
                    .orElseThrow(() -> new EntityNotFoundException("OpportunityUserEntity not found with id: " + opportunityUserDTO1.getId()));

            UserEntity user = userRepository.findById(Math.toIntExact(opportunityUserDTO1.getUserId()))
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + opportunityUserDTO1.getUserId()));
            OpportunityEntity opportunity = opportunityRepository.findById(opportunityUserDTO1.getOpportunityId())
                    .orElseThrow(() -> new EntityNotFoundException("Opportunity not found with id: " + opportunityUserDTO1.getOpportunityId()));

            // Check if the opportunity already has the required number of volunteers
            long currentVolunteerCount = opportunityUserRepository.countByOpportunityId(opportunityUserDTO1.getOpportunityId());
            if (currentVolunteerCount >= opportunity.getRequiredVolunteers()) {
                throw new CustomAuthenticationException("The opportunity has already reached the required number of volunteers.", null);
            }

            opportunityUserEntity.setUser(user);
            opportunityUserEntity.setOpportunity(opportunity);
            opportunityUserEntity.setStatus(opportunityUserDTO1.getStatus());
            opportunityUserEntity.setUpdatedAt(utils.date());
            opportunityUserEntity = opportunityUserRepository.save(opportunityUserEntity);

            OpportunityUserDTO opportunityUserDTO = new OpportunityUserDTO();
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer transitioned successfully");
            opportunityUserDTO.setResponseStatus(responseStatus);
            opportunityUserDTO.setId(opportunityUserEntity.getId());
            opportunityUserDTO.setStatus(opportunityUserEntity.getStatus());
            opportunityUserDTO.setUserId(user.getId());
            opportunityUserDTO.setUserName(user.getFullName());
            opportunityUserDTO.setUserEmail(user.getEmail());

            // Convert Set to List
            List<String> userSkills = new ArrayList<>(user.getVolunteer().getSkills());
            opportunityUserDTO.setUserSkills(userSkills);

            opportunityUserDTO.setOpportunityId(opportunity.getId());
            opportunityUserDTO.setOpportunityName(opportunity.getName());
            opportunityUserDTO.setOpportunityDescription(opportunity.getDescription());

            return opportunityUserDTO;
        } catch (DataAccessException e) {
            logger.error("Database error while transitioning: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while transitioning: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public Object getOpportunityUserByIdOrStatusOrUserIdOrOpportunityId(Long id, Boolean status, Long userId, Long opportunityId) {
        try {
            List<OpportunityUserEntity> opportunityUserEntities = new ArrayList<>();

            if (id != null) {
                Optional<OpportunityUserEntity> opportunityUserEntityOptional = opportunityUserRepository.findById(Math.toIntExact(id));
                OpportunityUserEntity opportunityUserEntity = opportunityUserEntityOptional
                        .orElseThrow(() -> new EntityNotFoundException("OpportunityUserEntity not found with id: " + id));
                opportunityUserEntities.add(opportunityUserEntity);
            } else {
                opportunityUserEntities = opportunityUserRepository.findAll(); // Start with all entities

                if (status != null) {
                    opportunityUserEntities = opportunityUserEntities.stream()
                            .filter(entity -> entity.getStatus().equals(status))
                            .collect(Collectors.toList());
                }

                if (userId != null) {
                    opportunityUserEntities = opportunityUserEntities.stream()
                            .filter(entity -> entity.getUser().getId().equals(userId))
                            .collect(Collectors.toList());
                }

                if (opportunityId != null) {
                    opportunityUserEntities = opportunityUserEntities.stream()
                            .filter(entity -> entity.getOpportunity() != null && entity.getOpportunity().getId().equals(opportunityId))
                            .collect(Collectors.toList());
                }
            }

            if (opportunityUserEntities.isEmpty()) {
                throw new EntityNotFoundException("No OpportunityUserEntities found with the given criteria.");
            }

            return opportunityUserEntities.stream().map(this::mapToDTO).collect(Collectors.toList());

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
            return opportunityUserEntities.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
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
        opportunityUserDTO.setId(opportunityUserEntity.getId());
        opportunityUserDTO.setUserId(opportunityUserEntity.getUser().getId());
        opportunityUserDTO.setUserName(opportunityUserEntity.getUser().getFullName());
        opportunityUserDTO.setUserEmail(opportunityUserEntity.getUser().getEmail());

        logger.info("opportunityUserEntity: {}", opportunityUserEntity);

        // Check if opportunity is not null before mapping
        if (opportunityUserEntity.getOpportunity() != null) {
            opportunityUserDTO.setOpportunityId(opportunityUserEntity.getOpportunity().getId());
            opportunityUserDTO.setOpportunityName(opportunityUserEntity.getOpportunity().getName());
            opportunityUserDTO.setOpportunityDescription(opportunityUserEntity.getOpportunity().getDescription());
        } else {
            // Handle the case where opportunity is null, if needed
            opportunityUserDTO.setOpportunityId(null);
            opportunityUserDTO.setOpportunityName("Opportunity not found"); // Example default value
            opportunityUserDTO.setOpportunityDescription("Opportunity details not available"); // Example default value
        }

        opportunityUserDTO.setStatus(opportunityUserEntity.getStatus());

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseCode("200");
        responseStatus.setResponseDesc("Fetched successfully");

        opportunityUserDTO.setResponseStatus(responseStatus);

        return opportunityUserDTO;
    }

    @Override
    public void deleteOpportunityUser(Long id) {
        try {
            OpportunityUserEntity opportunityUserEntity = opportunityUserRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("OpportunityUserEntity not found with id: " + id));
            opportunityUserRepository.delete(opportunityUserEntity);
            logger.info("Volunteer assignment with id {} has been deleted successfully.", id);

        } catch (DataAccessException e) {
            logger.error("Database error while deleting volunteer assignment: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while deleting volunteer assignment: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }
}
