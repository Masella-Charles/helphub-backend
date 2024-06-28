package com.volunteer.main.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.DonationDistributionEntity;
import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.DonationDistributionDTO;
import com.volunteer.main.model.response.DonationResponseDTO;
import com.volunteer.main.model.response.DonationDisasterResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.repositories.DisasterRepository;
import com.volunteer.main.repositories.DonationDistributionRepository;
import com.volunteer.main.repositories.DonationRepository;
import com.volunteer.main.service.DonationService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DonationServiceImpl implements DonationService {
    private static final Logger logger = LoggerFactory.getLogger(DonationServiceImpl.class);
    private final DonationRepository donationRepository;
    private final DisasterRepository disasterRepository;
    private final Utils utils;
    private final DonationDistributionRepository donationDistributionRepository;

    public DonationServiceImpl(DonationRepository donationRepository, DisasterRepository disasterRepository, Utils utils, DonationDistributionRepository donationDistributionRepository) {
        this.donationRepository = donationRepository;
        this.disasterRepository = disasterRepository;
        this.utils = utils;
        this.donationDistributionRepository = donationDistributionRepository;
    }

    @Override
    public ResponseEntity<?> createDonation(DonationDTO donationDTO) {
        try {
            DonationEntity donationEntity = new DonationEntity();
            donationEntity.setDonorName(donationDTO.getDonorName());
            donationEntity.setType(donationDTO.getType());
            donationEntity.setAmount(donationDTO.getAmount());
            donationEntity.setCommodityName(donationDTO.getCommodityName());
            donationEntity.setQuantity(donationDTO.getQuantity());
            donationEntity.setStatus(false);

            // Handle optional disasterId
            if (donationDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(donationDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + donationDTO.getDisasterId()));
                donationEntity.setDisasterEntity(disaster);
            }

            donationEntity = donationRepository.save(donationEntity);

            // Create response DTO
            DonationDTO donationSaved = new DonationDTO();
            donationSaved.setId(donationEntity.getId());
            donationSaved.setDonorName(donationEntity.getDonorName());
            donationSaved.setType(donationEntity.getType());
            donationSaved.setAmount(donationEntity.getAmount());
            donationSaved.setCommodityName(donationEntity.getCommodityName());
            donationSaved.setQuantity(donationEntity.getQuantity());
            donationSaved.setStatus(donationEntity.getStatus());

            // Only set disasterId if the disaster is not null
            if (donationEntity.getDisasterEntity() != null) {
                donationSaved.setDisasterId(donationEntity.getDisasterEntity().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation created successfully");

            DonationResponseDTO donationResponseDTO = new DonationResponseDTO();
            donationResponseDTO.setDonationDTO(donationSaved);
            donationResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(donationResponseDTO);
        } catch (DataAccessException e) {
            logger.error("Database error while creating donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }


    @Override
    public ResponseEntity<?> updateDonation(DonationDTO donationDTO) {
        try {
            DonationEntity donationEntity = donationRepository.findById(donationDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Donation not found with id: " + donationDTO.getId(), new Throwable()));

            donationEntity.setId(donationDTO.getId());
            donationEntity.setDonorName(donationDTO.getDonorName());
            donationEntity.setType(donationDTO.getType());
            donationEntity.setAmount(donationDTO.getAmount());
            donationEntity.setCommodityName(donationDTO.getCommodityName());
            donationEntity.setQuantity(donationDTO.getQuantity());

            if (donationDTO.getDisasterId() != null) {
                DisasterEntity disaster = disasterRepository.findById(donationDTO.getDisasterId())
                        .orElseThrow(() -> new EntityNotFoundException("Disaster not found with id: " + donationDTO.getDisasterId()));
                donationEntity.setDisasterEntity(disaster);
            }

            donationEntity = donationRepository.save(donationEntity);

            DonationDTO donationSaved = new DonationDTO();
            donationSaved.setId(donationEntity.getId());
            donationSaved.setDonorName(donationEntity.getDonorName());
            donationSaved.setType(donationEntity.getType());
            donationSaved.setAmount(donationEntity.getAmount());
            donationSaved.setCommodityName(donationEntity.getCommodityName());
            donationSaved.setQuantity(donationEntity.getQuantity());
            donationSaved.setStatus(donationEntity.getStatus());
//            donationSaved.setDisasterId(donationEntity.getDisasterEntity().getTId());

            // Only set disasterId if the disaster is not null
            if (donationEntity.getDisasterEntity() != null) {
                donationSaved.setDisasterId(donationEntity.getDisasterEntity().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation updated successfully");

            DonationResponseDTO donationResponseDTO = new DonationResponseDTO();
            donationResponseDTO.setDonationDTO(donationSaved);
            donationResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(donationResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while updating donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while updating donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> transitionDonation(DonationDTO donationDTO) {
        try {
            DonationEntity donationEntity = donationRepository.findById(donationDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Donation not found with id: " + donationDTO.getId(), new Throwable()));

            donationEntity.setId(donationDTO.getId());
            donationEntity.setStatus(donationDTO.getStatus());


            donationEntity = donationRepository.save(donationEntity);

            DonationDTO donationSaved = new DonationDTO();
            donationSaved.setId(donationEntity.getId());
            donationSaved.setDonorName(donationEntity.getDonorName());
            donationSaved.setType(donationEntity.getType());
            donationSaved.setAmount(donationEntity.getAmount());
            donationSaved.setCommodityName(donationEntity.getCommodityName());
            donationSaved.setQuantity(donationEntity.getQuantity());
            donationSaved.setStatus(donationEntity.getStatus());


            // Only set disasterId if the disaster is not null
            if (donationEntity.getDisasterEntity() != null) {
                donationSaved.setDisasterId(donationEntity.getDisasterEntity().getId());
            }else{
                donationSaved.setDisasterId(null);
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation transitioned successfully");

            DonationResponseDTO donationResponseDTO = new DonationResponseDTO();
            donationResponseDTO.setDonationDTO(donationSaved);
            donationResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(donationResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while transitioning donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while transitioning donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getDonationById(DonationDTO donationDTO) {
        try {
            DonationEntity donationEntity = donationRepository.findById(donationDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Donation not found with id: " + donationDTO.getId(), new Throwable()));


            logger.info("donationEntity: {}", donationEntity);
            donationEntity.setId(donationDTO.getId());


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(donationEntity);

            DonationDisasterResponseDTO donationDisasterResponseDTO = (DonationDisasterResponseDTO) utils.setJsonStringToObject(json.toString(), DonationDisasterResponseDTO.class);
            donationDisasterResponseDTO.setId(donationEntity.getId());
            logger.info("donationDisasterResponseDTO: {}", donationDisasterResponseDTO);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation fetched successfully");

            donationDisasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(donationDisasterResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while fetching donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching donati on: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DonationEntity> getAllDonations() {
        try {
            Iterable<DonationEntity> disasterEntityIterable = donationRepository.findAll();
            return StreamSupport.stream(disasterEntityIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while listing all donations: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all donations: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getDonationByIdOrStatusOrDisasterId(DonationDTO donationDTO) {
        try {
            Long id = donationDTO.getId();
            Boolean status = donationDTO.getStatus();
            Long disasterId = donationDTO.getDisasterId();

            DonationEntity donationEntity = null;
            if (id != null) {
                donationEntity = donationRepository.findById(id)
                        .orElseThrow(() -> new CustomAuthenticationException("Donation not found with id: " + id,null));
            } else if (status != null) {
                List<DonationEntity> donationEntities = donationRepository.findByStatus(status);
                if (!donationEntities.isEmpty()) {
                    donationEntity = donationEntities.get(0); // Assuming you need only one
                } else {
                    throw new EntityNotFoundException("No donations found with status: " + status);
                }
            } else if (disasterId != null) {
                List<DonationEntity> donationEntities = donationRepository.findByDisasterEntityId(disasterId);
                if (!donationEntities.isEmpty()) {
                    donationEntity = donationEntities.get(0); // Assuming you need only one
                } else {
                    throw new EntityNotFoundException("No donations found for disaster with id: " + disasterId);
                }
            } else {
                throw new IllegalArgumentException("Either id, status, or disasterId must be provided");
            }

            // Mapping to DTO
            DonationDisasterResponseDTO donationDisasterResponseDTO = mapToDTO(donationEntity);

            // Setting response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Donation fetched successfully");
            donationDisasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(donationDisasterResponseDTO);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found while fetching donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Entity not found: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching donation: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private DonationDisasterResponseDTO mapToDTO(DonationEntity donationEntity) {
        DonationDisasterResponseDTO donationDisasterResponseDTO = new DonationDisasterResponseDTO();
        donationDisasterResponseDTO.setId(donationEntity.getId());
        donationDisasterResponseDTO.setDonorName(donationEntity.getDonorName());
        donationDisasterResponseDTO.setType(donationEntity.getType());
        donationDisasterResponseDTO.setAmount(donationEntity.getAmount());
        donationDisasterResponseDTO.setCommodityName(donationEntity.getCommodityName());
        donationDisasterResponseDTO.setQuantity(donationEntity.getQuantity());
        donationDisasterResponseDTO.setStatus(donationEntity.getStatus());

        // Mapping DisasterEntity
        DonationDisasterResponseDTO.DisasterEntity disasterEntityDTO = new DonationDisasterResponseDTO.DisasterEntity();
        DisasterEntity disasterEntity = donationEntity.getDisasterEntity();
        if (disasterEntity != null) {
            disasterEntityDTO.setTId(disasterEntity.getId());
            disasterEntityDTO.setName(disasterEntity.getName());
            disasterEntityDTO.setDescription(disasterEntity.getDescription());
            disasterEntityDTO.setDate(disasterEntity.getDate());
            disasterEntityDTO.setStatus(disasterEntity.getStatus());
        }
        donationDisasterResponseDTO.setDisasterEntity(disasterEntityDTO);

        return donationDisasterResponseDTO;
    }

    private boolean canDistributeFurther(DonationEntity donationEntity, DonationDistributionDTO donationDistributionDTO) {
        // Calculate remaining quantity or amount
        Integer remainingQuantity = donationEntity.getQuantity() - getAlreadyDistributedQuantity(donationEntity);
        Double remainingAmount = donationEntity.getAmount() - getAlreadyDistributedAmount(donationEntity);

        // Check against intended distribution quantity or amount
        if ("money".equals(donationEntity.getType())) {
            return remainingAmount >= donationDistributionDTO.getAmountDistributed();
        } else if ("commodity".equals(donationEntity.getType())) {
            return remainingQuantity >= donationDistributionDTO.getQuantityDistributed();
        } else {
            // Handle other types or throw an exception if type is unknown
            throw new IllegalArgumentException("Unknown donation type: " + donationEntity.getType());
        }
    }

    private Integer getAlreadyDistributedQuantity(DonationEntity donationEntity) {
        // Example: Fetch already distributed quantity from DonationDistributionEntity
        List<DonationDistributionEntity> distributions = donationDistributionRepository.findByDonationId(donationEntity.getId());
        int alreadyDistributedQuantity = distributions.stream().mapToInt(DonationDistributionEntity::getQuantityDistributed).sum();
        return alreadyDistributedQuantity;
    }

    private Double getAlreadyDistributedAmount(DonationEntity donationEntity) {
        // Example: Fetch already distributed amount from DonationDistributionEntity
        List<DonationDistributionEntity> distributions = donationDistributionRepository.findByDonationId(donationEntity.getId());
        double alreadyDistributedAmount = distributions.stream().mapToDouble(DonationDistributionEntity::getAmountDistributed).sum();
        return alreadyDistributedAmount;
    }

    @Override
    public ResponseEntity<?> createDonationDistribution(DonationDistributionDTO donationDistributionDTO) {
        logger.info("Creating donation distribution for DTO: {}", donationDistributionDTO);
        try {
            DonationEntity donationEntity = donationRepository.findById(donationDistributionDTO.getDonationId())
                    .orElseThrow(() -> new EntityNotFoundException("Donation not found with id: " + donationDistributionDTO.getDonationId()));

            logger.info("Found donation entity: {}", donationEntity);

            DonationDistributionEntity donationDistributionEntity = new DonationDistributionEntity();
            donationDistributionEntity.setRecipientName(donationDistributionDTO.getRecipientName());
            donationDistributionEntity.setAmountDistributed(donationDistributionDTO.getAmountDistributed());
            donationDistributionEntity.setQuantityDistributed(donationDistributionDTO.getQuantityDistributed());
            donationDistributionEntity.setDonation(donationEntity);

            DonationDistributionEntity savedEntity = donationDistributionRepository.save(donationDistributionEntity);

            logger.info("Saved donation distribution entity: {}", savedEntity);

            DonationDistributionDTO savedDTO = mapDonationDistributionToDTO(savedEntity);
            return ResponseEntity.ok().body(savedDTO);
        } catch (EntityNotFoundException e) {
            logger.error("Error creating donation distribution: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating donation distribution: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create donation distribution: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateDonationDistribution(Long id, DonationDistributionDTO donationDistributionDTO) {
        try {
            DonationDistributionEntity existingEntity = donationDistributionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Donation distribution not found with id: " + id));

            updateDonationDistributionEntityFromDTO(existingEntity, donationDistributionDTO);

            DonationDistributionEntity updatedEntity = donationDistributionRepository.save(existingEntity);
            DonationDistributionDTO updatedDTO = mapDonationDistributionToDTO(updatedEntity);
            return ResponseEntity.ok().body(updatedDTO);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update donation distribution: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteDonationDistribution(Long id) {
        try {
            DonationDistributionEntity existingEntity = donationDistributionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Donation distribution not found with id: " + id));

            donationDistributionRepository.delete(existingEntity);
            return ResponseEntity.ok()
                    .body("{\"responseEntity\": {\"status\": 200, \"body\": \"Donation distribution deleted successfully\"}}");
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"responseEntity\": {\"status\": 500, \"body\": \"Failed to delete donation distribution: " + e.getMessage() + "\"}}");
        }
    }

    @Override
    public ResponseEntity<?> getDonationDistributionById(Long id) {
        try {
            DonationDistributionEntity donationDistributionEntity = donationDistributionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Donation distribution not found with id: " + id));

            DonationDistributionDTO donationDistributionDTO = mapDonationDistributionToDTO(donationDistributionEntity);
            return ResponseEntity.ok().body(donationDistributionDTO);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch donation distribution: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllDonationDistributions() {
        try {
            Iterable<DonationDistributionEntity> donationDistributionEntities = donationDistributionRepository.findAll();
            List<DonationDistributionDTO> donationDistributionDTOs = StreamSupport.stream(donationDistributionEntities.spliterator(), false)
                    .map(this::mapDonationDistributionToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(donationDistributionDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch all donation distributions: " + e.getMessage());
        }
    }

    private DonationDistributionEntity mapDonationDistributionToEntity(DonationDistributionDTO dto) {
        DonationDistributionEntity entity = new DonationDistributionEntity();
        entity.setRecipientName(dto.getRecipientName());
        entity.setAmountDistributed(dto.getAmountDistributed());
        entity.setQuantityDistributed(dto.getQuantityDistributed());
        // Mapping to DonationEntity if needed
        // entity.setDonation(...);
        return entity;
    }

    private DonationDistributionDTO mapDonationDistributionToDTO(DonationDistributionEntity entity) {
        DonationDistributionDTO dto = new DonationDistributionDTO();
        dto.setId(entity.getId());
        dto.setRecipientName(entity.getRecipientName());
        dto.setAmountDistributed(entity.getAmountDistributed());
        dto.setQuantityDistributed(entity.getQuantityDistributed());

        // Fetch and map fields from DonationEntity
        DonationEntity donationEntity = entity.getDonation();
        if (donationEntity != null) {
            dto.setDonationId(donationEntity.getId());
            dto.setDonorName(donationEntity.getDonorName());
            dto.setDonationType(donationEntity.getType());
            dto.setDonationAmount(donationEntity.getAmount());
            dto.setCommodityName(donationEntity.getCommodityName());
            dto.setDonationQuantity(donationEntity.getQuantity());
            dto.setDonationStatus(donationEntity.getStatus());
        }

        return dto;
    }

    private void updateDonationDistributionEntityFromDTO(DonationDistributionEntity entity, DonationDistributionDTO dto) {
        entity.setRecipientName(dto.getRecipientName());
        entity.setAmountDistributed(dto.getAmountDistributed());
        entity.setQuantityDistributed(dto.getQuantityDistributed());
        // Update DonationEntity if needed
        // entity.getDonation().set...();
    }
}
