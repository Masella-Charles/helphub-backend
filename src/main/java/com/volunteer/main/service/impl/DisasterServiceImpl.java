package com.volunteer.main.service.impl;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.DisasterDTO;
import com.volunteer.main.model.response.DisasterResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.repositories.DisasterRepository;
import com.volunteer.main.service.DisasterService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DisasterServiceImpl implements DisasterService {
    private static final Logger logger = LoggerFactory.getLogger(DisasterServiceImpl.class);
    private final DisasterRepository disasterRepository;
    private final Utils utils;

    public DisasterServiceImpl(DisasterRepository disasterRepository, Utils utils) {
        this.disasterRepository = disasterRepository;
        this.utils = utils;
    }

    @Override
    public ResponseEntity<?> createDisaster(DisasterDTO disasterDTO) {
        try{
            DisasterEntity disasterEntity = new DisasterEntity();
            disasterEntity.setName(disasterDTO.getName());
            disasterEntity.setDescription(disasterDTO.getDescription());
            disasterEntity.setStatus(false);
            disasterEntity.setDate(utils.date());

            disasterEntity = disasterRepository.save(disasterEntity);

            DisasterDTO disasterSaved = new DisasterDTO();
            disasterSaved.setId(disasterEntity.getId());
            disasterSaved.setName(disasterEntity.getName());
            disasterSaved.setDescription(disasterEntity.getDescription());
            disasterSaved.setStatus(disasterEntity.getStatus());
            disasterSaved.setDate(disasterEntity.getDate());

            logger.info("Permission created: {}", disasterSaved);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Disaster created successfully");

            DisasterResponseDTO disasterResponseDTO = new DisasterResponseDTO();
            disasterResponseDTO.setDisasterDTO(disasterSaved);
            disasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(disasterResponseDTO);

        } catch (DataAccessException e) {
            logger.error("Database error while creating disaster: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating disaster: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> updateDisaster(DisasterDTO disasterDTO) {
        try {
            DisasterEntity disasterEntity = disasterRepository.findById(disasterDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Disaster not found with id: " + disasterDTO.getId(), new Throwable()));

            disasterEntity.setId(disasterDTO.getId());
            disasterEntity.setName(disasterDTO.getName());
            disasterEntity.setDescription(disasterDTO.getDescription());

            disasterEntity = disasterRepository.save(disasterEntity);

            DisasterDTO disasterSaved = new DisasterDTO();
            disasterSaved.setId(disasterEntity.getId());
            disasterSaved.setName(disasterEntity.getName());
            disasterSaved.setDescription(disasterEntity.getDescription());
            disasterSaved.setStatus(disasterEntity.getStatus());
            disasterSaved.setDate(disasterEntity.getDate());

            // Log the updated permission
            logger.info("Disaster updated: {}", disasterSaved);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Disaster updated successfully");

            DisasterResponseDTO disasterResponseDTO = new DisasterResponseDTO();
            disasterResponseDTO.setDisasterDTO(disasterSaved);
            disasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(disasterResponseDTO);
        } catch (DataAccessException e) {
            logger.error("Database error while updating disaster: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while updating disaster: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> transitionDisaster(DisasterDTO disasterDTO) {
        try {
            DisasterEntity disasterEntity = disasterRepository.findById(disasterDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Disaster not found with id: " + disasterDTO.getId(), new Throwable()));

            disasterEntity.setId(disasterDTO.getId());
            disasterEntity.setStatus(disasterDTO.getStatus());
            disasterEntity = disasterRepository.save(disasterEntity);

            DisasterDTO disasterSaved = new DisasterDTO();
            disasterSaved.setId(disasterEntity.getId());
            disasterSaved.setName(disasterEntity.getName());
            disasterSaved.setDescription(disasterEntity.getDescription());
            disasterSaved.setStatus(disasterEntity.getStatus());
            disasterSaved.setDate(disasterEntity.getDate());

            // Log the updated permission
            logger.info("Disaster transitioned: {}", disasterSaved);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Disaster transitioned successfully");

            DisasterResponseDTO disasterResponseDTO = new DisasterResponseDTO();
            disasterResponseDTO.setDisasterDTO(disasterSaved);
            disasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(disasterResponseDTO);
        } catch (CustomAuthenticationException e) {
            logger.error("Database error while transitioning disaster: {}", e.getMessage());
            throw  e;
        } catch (Exception e) {
            logger.error("Unexpected error while transitioning disaster: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getDisasterById(DisasterDTO disasterDTO) {
        try {
            DisasterEntity disasterEntity = disasterRepository.findById(disasterDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Disaster not found with id: " + disasterDTO.getId(), new Throwable()));

            disasterEntity.setId(disasterDTO.getId());

            disasterEntity = disasterRepository.save(disasterEntity);

            DisasterDTO disasterSaved = new DisasterDTO();
            disasterSaved.setId(disasterEntity.getId());
            disasterSaved.setName(disasterEntity.getName());
            disasterSaved.setDescription(disasterEntity.getDescription());
            disasterSaved.setStatus(disasterEntity.getStatus());
            disasterSaved.setDate(disasterEntity.getDate());

            // Log the updated permission
            logger.info("Disaster updated: {}", disasterSaved);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Disaster fetched successfully");

            DisasterResponseDTO disasterResponseDTO = new DisasterResponseDTO();
            disasterResponseDTO.setDisasterDTO(disasterSaved);
            disasterResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(disasterResponseDTO);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomAuthenticationException("Failed to get disaster: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getDisasterByIdOrStatus(Long id, Boolean status) {
        try {
            Optional<DisasterEntity> disasterEntityOptional = disasterRepository.findByIdOrStatus(id, status);
            DisasterEntity disasterEntity = disasterEntityOptional.orElseThrow(() -> {
                if (id != null) {
                    return new EntityNotFoundException("Disaster not found with ID: " + id);
                } else {
                    return new EntityNotFoundException("Disaster not found with status: " + status);
                }
            });

            // Map entity to DTO if needed
            DisasterDTO disasterDTO = mapToDTO(disasterEntity);

            return ResponseEntity.ok().body(disasterDTO);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomAuthenticationException("Failed to get disaster: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DisasterEntity> getAllDisasters() {
        try {
            Iterable<DisasterEntity> disasterEntityIterable = disasterRepository.findAll();
            return StreamSupport.stream(disasterEntityIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomAuthenticationException("Failed to get all disasters: " + e.getMessage(), e);
        }
    }

    // Utility method to map Entity to DTO
    private DisasterDTO mapToDTO(DisasterEntity disasterEntity) {
        DisasterDTO disasterDTO = new DisasterDTO();
        disasterDTO.setId(disasterEntity.getId());
        disasterDTO.setName(disasterEntity.getName());
        disasterDTO.setDescription(disasterEntity.getDescription());
        disasterDTO.setDate(disasterEntity.getDate());
        disasterDTO.setStatus(disasterEntity.getStatus());
        return disasterDTO;
    }
}
