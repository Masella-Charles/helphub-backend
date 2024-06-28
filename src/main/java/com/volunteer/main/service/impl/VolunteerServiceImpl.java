package com.volunteer.main.service.impl;

import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.entity.VolunteerEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.VolunteerDto;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.model.response.VolunteerResponseDTO;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.repositories.VolunteerRepository;
import com.volunteer.main.service.VolunteerService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerServiceImpl.class);
    private final VolunteerRepository volunteerRepository;
    private final Utils utils;
    private final UserRepository userRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository, Utils utils, UserRepository userRepository) {
        this.volunteerRepository = volunteerRepository;
        this.utils = utils;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> createVolunteer(VolunteerDto volunteerDto) {
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findById(volunteerDto.getUserId());
            if (userEntityOptional.isEmpty()) {
                throw new IllegalArgumentException("User ID is invalid");
            }

            VolunteerEntity volunteer = new VolunteerEntity();
            volunteer.setUser(userEntityOptional.get());
            volunteer.setPhone(volunteerDto.getPhone());
            volunteer.setAdditionalInfo(volunteerDto.getAdditionalInfo());
            volunteer.setSkills(new HashSet<>(volunteerDto.getSkills()));
            volunteer.setStatus(false);
            volunteer = volunteerRepository.save(volunteer);

            // Map VolunteerEntity to VolunteerDto
            ModelMapper modelMapper = new ModelMapper();
            VolunteerDto volunteerDto1 = modelMapper.map(volunteer, VolunteerDto.class);
            volunteerDto1.setId(volunteer.getId());
            volunteerDto1.setName(userEntityOptional.get().getFullName()); // Assuming you want to include the user's name

            // Prepare response
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer created successfully.");

            VolunteerResponseDTO volunteerResponseDTO = new VolunteerResponseDTO();
            volunteerResponseDTO.setVolunteer(volunteerDto1);
            volunteerResponseDTO.setResponseStatus(responseStatus);

            logger.info("Volunteer created: {}", volunteerResponseDTO);

            return ResponseEntity.ok().body(volunteerResponseDTO);
        } catch (DataIntegrityViolationException e) {
            logger.error("Database error while creating volunteer: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating volunteer: {}", e.getMessage());
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
                    .orElseThrow(() -> new CustomAuthenticationException("Volunteer not found with id: " + volunteerDto.getId(), new Throwable()));

            Optional<UserEntity> userEntityOptional = userRepository.findById(volunteerDto.getUserId());
            if (userEntityOptional.isEmpty()) {
                throw new IllegalArgumentException("User ID is invalid");
            }

            // Update the existing volunteer entity with the new data from volunteerDto
            // Exclude copying id and status (assuming status should not be updated)
            BeanUtils.copyProperties(volunteerDto, existingVolunteer, "id", "status");

            existingVolunteer.setSkills(new HashSet<>(volunteerDto.getSkills()));

            // Save the updated volunteer entity
            volunteerRepository.save(existingVolunteer);

            // Prepare the response DTO
            VolunteerDto updatedVolunteerDto = new VolunteerDto();
            updatedVolunteerDto.setId(existingVolunteer.getId());
            updatedVolunteerDto.setName(existingVolunteer.getUser().getFullName()); // Assuming you want to include the user's name
            updatedVolunteerDto.setPhone(existingVolunteer.getPhone());
            updatedVolunteerDto.setEmail(existingVolunteer.getUser().getEmail());
            updatedVolunteerDto.setAdditionalInfo(existingVolunteer.getAdditionalInfo());
            updatedVolunteerDto.setSkills(new ArrayList<>(existingVolunteer.getSkills()));
            updatedVolunteerDto.setUserId(Math.toIntExact(existingVolunteer.getUser().getId()));

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Volunteer updated successfully.");

            VolunteerResponseDTO volunteerResponseDTO = new VolunteerResponseDTO();
            volunteerResponseDTO.setVolunteer(updatedVolunteerDto);
            volunteerResponseDTO.setResponseStatus(responseStatus);

            logger.info("Volunteer updated: {}", volunteerResponseDTO);

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


