package com.volunteer.main.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.response.DonationResponseDTO;
import com.volunteer.main.model.response.DonationDisasterResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.repositories.DisasterRepository;
import com.volunteer.main.repositories.DonationRepository;
import com.volunteer.main.service.DonationService;
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
public class DonationServiceImpl implements DonationService {
    private static final Logger logger = LoggerFactory.getLogger(DonationServiceImpl.class);
    private final DonationRepository donationRepository;
    private final DisasterRepository disasterRepository;
    private final Utils utils;

    public DonationServiceImpl(DonationRepository donationRepository, DisasterRepository disasterRepository, Utils utils) {
        this.donationRepository = donationRepository;
        this.disasterRepository = disasterRepository;
        this.utils = utils;
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
            donationSaved.setId(donationEntity.getTId());
            donationSaved.setDonorName(donationEntity.getDonorName());
            donationSaved.setType(donationEntity.getType());
            donationSaved.setAmount(donationEntity.getAmount());
            donationSaved.setCommodityName(donationEntity.getCommodityName());
            donationSaved.setQuantity(donationEntity.getQuantity());
            donationSaved.setStatus(donationEntity.getStatus());

            // Only set disasterId if the disaster is not null
            if (donationEntity.getDisasterEntity() != null) {
                donationSaved.setDisasterId(donationEntity.getDisasterEntity().getTId());
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

            donationEntity.setTId(donationDTO.getId());
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
            donationSaved.setId(donationEntity.getTId());
            donationSaved.setDonorName(donationEntity.getDonorName());
            donationSaved.setType(donationEntity.getType());
            donationSaved.setAmount(donationEntity.getAmount());
            donationSaved.setCommodityName(donationEntity.getCommodityName());
            donationSaved.setQuantity(donationEntity.getQuantity());
            donationSaved.setStatus(donationEntity.getStatus());
//            donationSaved.setDisasterId(donationEntity.getDisasterEntity().getTId());

            // Only set disasterId if the disaster is not null
            if (donationEntity.getDisasterEntity() != null) {
                donationSaved.setDisasterId(donationEntity.getDisasterEntity().getTId());
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

            donationEntity.setTId(donationDTO.getId());
            donationEntity.setStatus(donationDTO.getStatus());


            donationEntity = donationRepository.save(donationEntity);

            DonationDTO donationSaved = new DonationDTO();
            donationSaved.setId(donationEntity.getTId());
            donationSaved.setDonorName(donationEntity.getDonorName());
            donationSaved.setType(donationEntity.getType());
            donationSaved.setAmount(donationEntity.getAmount());
            donationSaved.setCommodityName(donationEntity.getCommodityName());
            donationSaved.setQuantity(donationEntity.getQuantity());
            donationSaved.setStatus(donationEntity.getStatus());


            // Only set disasterId if the disaster is not null
            if (donationEntity.getDisasterEntity() != null) {
                donationSaved.setDisasterId(donationEntity.getDisasterEntity().getTId());
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
            donationEntity.setTId(donationDTO.getId());


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(donationEntity);

            DonationDisasterResponseDTO donationDisasterResponseDTO = (DonationDisasterResponseDTO) utils.setJsonStringToObject(json.toString(), DonationDisasterResponseDTO.class);
            donationDisasterResponseDTO.setId(donationEntity.getTId());
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
}
