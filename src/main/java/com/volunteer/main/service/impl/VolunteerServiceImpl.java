package com.volunteer.main.service.impl;

import com.volunteer.main.entity.VolunteerEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.VolunteerDto;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.model.response.VolunteerResponseDTO;
import com.volunteer.main.repositories.VolunteerRepository;
import com.volunteer.main.service.VolunteerService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerServiceImpl.class);
    private final VolunteerRepository volunteerRepository;
    private final Utils utils;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository, Utils utils) {
        this.volunteerRepository = volunteerRepository;
        this.utils = utils;
    }

    @Override
    @Transactional
    public ResponseEntity<?> createVolunteer(VolunteerDto volunteerDto) {

        try {
            VolunteerEntity volunteer = new VolunteerEntity();
            volunteer.setName(volunteerDto.getName());
            volunteer.setEmail(volunteerDto.getEmail());
            volunteer.setPhone(volunteerDto.getPhone());
            volunteer.setAdditionalInfo(volunteerDto.getAdditionalInfo());
            volunteer.setStatus(false);
            volunteer = volunteerRepository.save(volunteer);

            JSONObject volunteerJsonObject = new JSONObject(volunteer);

            VolunteerDto volunteerDto1 = (VolunteerDto) utils.setJsonStringToObject(volunteerJsonObject.toString(),
                    VolunteerDto.class);
            volunteerDto1.setId(volunteer.getTId());
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer created successfully.");
            VolunteerResponseDTO volunteerResponseDTO = new VolunteerResponseDTO();
            volunteerResponseDTO.setVolunteer(volunteerDto1);
            volunteerResponseDTO.setResponseStatus(responseStatus);

            JSONObject jsonObject = new JSONObject(volunteerResponseDTO);

            logger.info("Volunteer created :\n {}", jsonObject);

            return ResponseEntity.ok().body(volunteerResponseDTO);
        } catch (DataIntegrityViolationException  e){
            logger.error("Database error while creating volunteer: {}", e.getMessage());

            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        }
        catch (Exception e) {
            logger.error("Unexpected error while creating volunteer: {}", e.getMessage());
            // Handle unexpected exception (e.g., return a specific error response)
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VolunteerEntity> listAllVolunteers() {
        try {
            Iterable<VolunteerEntity> volunteerIterable = volunteerRepository.findAll();
            return StreamSupport.stream(volunteerIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while listing all volunteers: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all volunteers: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> updateVolunteer(VolunteerDto volunteerDto) {
        try {
            // Retrieve the existing volunteer entity from the database
            VolunteerEntity existingVolunteer = volunteerRepository.findById(volunteerDto.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Volunteer not found with id: " + volunteerDto.getId(),new Throwable()));

            // Update the existing volunteer entity with the new data from volunteer
            BeanUtils.copyProperties(volunteerDto, existingVolunteer, "id"); // Exclude copying id

            // Save and return the updated volunteer entity
             volunteerRepository.save(existingVolunteer);

            JSONObject volunteerJsonObject = new JSONObject(existingVolunteer);

            VolunteerDto volunteerDto1 = (VolunteerDto) utils.setJsonStringToObject(volunteerJsonObject.toString(),
                    VolunteerDto.class);
            volunteerDto1.setId(existingVolunteer.getTId());
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer updated successfully.");
            VolunteerResponseDTO volunteerResponseDTO = new VolunteerResponseDTO();
            volunteerResponseDTO.setVolunteer(volunteerDto1);
            volunteerResponseDTO.setResponseStatus(responseStatus);

            JSONObject jsonObject = new JSONObject(volunteerResponseDTO);

            logger.info("Volunteer updated :\n {}", jsonObject);
            return ResponseEntity.ok().body(volunteerResponseDTO);
        } catch (EntityNotFoundException e) {
            // Log the error or rethrow to let the caller handle it
            throw e;
        } catch (Exception e) {
            // You can log the error for debugging purposes
            e.printStackTrace();
            // Rethrow a more generic exception or handle as appropriate
            throw new CustomAuthenticationException("Failed to update volunteer: " + e.getMessage(), e);
        }
    }

    @Override
    public VolunteerEntity getVolunteerById(Long id) {
        try {
            // Retrieve the volunteer entity from the database by ID

            return volunteerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Volunteer not found with id: " + id));
        } catch (EntityNotFoundException e) {
            // Log the error or rethrow to let the caller handle it
            throw e;
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            // Rethrow a more generic exception or handle as appropriate
            throw new CustomAuthenticationException("Failed to get volunteer: " + e.getMessage(), e);
        }
    }
}


