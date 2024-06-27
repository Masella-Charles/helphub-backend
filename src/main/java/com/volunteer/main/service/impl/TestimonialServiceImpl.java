package com.volunteer.main.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volunteer.main.entity.TestimonialEntity;
import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.TestimonialDTO;
import com.volunteer.main.model.response.*;
import com.volunteer.main.repositories.TestimonialRepository;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.TestimonialService;
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
public class TestimonialServiceImpl implements TestimonialService {
    private static final Logger logger = LoggerFactory.getLogger(TestimonialServiceImpl.class);
    private final UserRepository userRepository;
    private final TestimonialRepository testimonialRepository;
    private final Utils utils;

    public TestimonialServiceImpl(UserRepository userRepository, TestimonialRepository testimonialRepository, Utils utils) {
        this.userRepository = userRepository;
        this.testimonialRepository = testimonialRepository;
        this.utils = utils;
    }


    @Override
    public ResponseEntity<?> createTestimonial(TestimonialDTO testimonialDTO) {
        try {
            TestimonialEntity testimonialEntity = new TestimonialEntity();
            testimonialEntity.setTestimonial(testimonialDTO.getTestimonial());
            testimonialEntity.setCreatedAt(utils.date());
            testimonialEntity.setStatus(false);


            if (testimonialDTO.getUserId() != null) {
                UserEntity user = userRepository.findById(Math.toIntExact(testimonialDTO.getUserId()))
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + testimonialDTO.getUserId()));
                testimonialEntity.setUser(user);
            }

            testimonialEntity = testimonialRepository.save(testimonialEntity);

            // Create response DTO
            TestimonialDTO testimonialSaved = new TestimonialDTO();
            testimonialSaved.setId(testimonialEntity.getId());
            testimonialSaved.setTestimonial(testimonialEntity.getTestimonial());
            testimonialSaved.setCreatedAt(testimonialEntity.getCreatedAt());
            testimonialSaved.setStatus(testimonialEntity.getStatus());

            if (testimonialEntity.getUser() != null) {
                testimonialSaved.setUserId(testimonialEntity.getUser().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Testimonial created successfully");

            TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
            testimonialResponseDTO.setTestimonialDTO(testimonialSaved);
            testimonialResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(testimonialResponseDTO);
        } catch (DataAccessException e) {
            logger.error("Database error while creating testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }


    @Override
    public ResponseEntity<?> updateTestimonial(TestimonialDTO testimonialDTO) {
        try {
            TestimonialEntity testimonialEntity = testimonialRepository.findById(testimonialDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Testimonial not found with id: " + testimonialDTO.getId(), new Throwable()));

            testimonialEntity.setId(testimonialDTO.getId());
            testimonialEntity.setTestimonial(testimonialDTO.getTestimonial());

            if (testimonialDTO.getUserId() != null) {
                UserEntity user = userRepository.findById(Math.toIntExact(testimonialDTO.getUserId()))
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + testimonialDTO.getUserId()));
                testimonialEntity.setUser(user);
            }

            testimonialEntity = testimonialRepository.save(testimonialEntity);

            TestimonialDTO testimonialSaved = new TestimonialDTO();
            testimonialSaved.setId(testimonialEntity.getId());
            testimonialSaved.setTestimonial(testimonialEntity.getTestimonial());
            testimonialSaved.setCreatedAt(testimonialEntity.getCreatedAt());
            testimonialSaved.setStatus(testimonialEntity.getStatus());


            if (testimonialEntity.getUser() != null) {
                testimonialSaved.setUserId(testimonialEntity.getUser().getId());
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Testimonial updated successfully");

            TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
            testimonialResponseDTO.setTestimonialDTO(testimonialSaved);
            testimonialResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(testimonialResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while updating testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while updating testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> transitionTestimonial(TestimonialDTO testimonialDTO) {
        try {
            TestimonialEntity testimonialEntity = testimonialRepository.findById(testimonialDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Testimonial not found with id: " + testimonialDTO.getId(), new Throwable()));

            testimonialEntity.setId(testimonialDTO.getId());
            testimonialEntity.setStatus(testimonialDTO.getStatus());


            testimonialEntity = testimonialRepository.save(testimonialEntity);

            TestimonialDTO testimonialSaved = new TestimonialDTO();
            testimonialSaved.setId(testimonialEntity.getId());
            testimonialSaved.setTestimonial(testimonialEntity.getTestimonial());
            testimonialSaved.setCreatedAt(testimonialEntity.getCreatedAt());
            testimonialSaved.setStatus(testimonialEntity.getStatus());


            if (testimonialEntity.getUser() != null) {
                testimonialSaved.setUserId(testimonialEntity.getUser().getId());
            }else{
                testimonialSaved.setUserId(null);
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Testimonial transitioned successfully");

            TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
            testimonialResponseDTO.setTestimonialDTO(testimonialSaved);
            testimonialResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(testimonialResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while transitioning testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while transitioning testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getTestimonialById(TestimonialDTO testimonialDTO) {
        try {
            TestimonialEntity testimonialEntity = testimonialRepository.findById(testimonialDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Testimonial not found with id: " + testimonialDTO.getId(), new Throwable()));


            logger.info("testimonialEntity: {}", testimonialEntity);
            testimonialEntity.setId(testimonialDTO.getId());


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(testimonialEntity);

            TestimonialUserResponseDTO testimonialUserResponseDTO = (TestimonialUserResponseDTO) utils.setJsonStringToObject(json.toString(), TestimonialUserResponseDTO.class);
            testimonialUserResponseDTO.setId(testimonialEntity.getId());
            logger.info("testimonialUserResponseDTO: {}", testimonialUserResponseDTO);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Testimonial fetched successfully");

            testimonialUserResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(testimonialUserResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while fetching testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TestimonialEntity> getAllTestimonials() {
        try {
            Iterable<TestimonialEntity> testimonialEntityIterable = testimonialRepository.findAll();
            return StreamSupport.stream(testimonialEntityIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while listing all testimonials: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all testimonials: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getTestimonialByCriteria(Long id, Boolean status, Long userId) {
        try {
            if (id != null) {
                TestimonialEntity testimonialEntity = testimonialRepository.findById(id)
                        .orElseThrow(() -> new CustomAuthenticationException("Testimonial not found with id: " + id, new Throwable()));
                return ResponseEntity.ok().body(mapToDTO(testimonialEntity));
            } else if (status != null) {
                List<TestimonialEntity> testimonialEntities = testimonialRepository.findByStatus(status);
                return ResponseEntity.ok().body(testimonialEntities.stream().map(this::mapToDTO).collect(Collectors.toList()));
            } else if (userId != null) {
                List<TestimonialEntity> testimonialEntities = testimonialRepository.findByUserId(userId);
                return ResponseEntity.ok().body(testimonialEntities.stream().map(this::mapToDTO).collect(Collectors.toList()));
            } else {
                throw new IllegalArgumentException("Either id, status, or userId must be provided");
            }
        } catch (DataAccessException e) {
            logger.error("Database error while fetching testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private TestimonialUserResponseDTO mapToDTO(TestimonialEntity testimonialEntity) {
        TestimonialUserResponseDTO dto = new TestimonialUserResponseDTO();
        dto.setId(testimonialEntity.getId());
        dto.setTestimonial(testimonialEntity.getTestimonial());
        dto.setStatus(testimonialEntity.getStatus());
        dto.setCreatedAt(testimonialEntity.getCreatedAt());

        TestimonialUserResponseDTO.UserEntity userEntityDTO = new TestimonialUserResponseDTO.UserEntity();
        userEntityDTO.setTId(testimonialEntity.getUser().getId());
        userEntityDTO.setFullName(testimonialEntity.getUser().getFullName());
        userEntityDTO.setEmail(testimonialEntity.getUser().getEmail());
        userEntityDTO.setPassword(testimonialEntity.getUser().getPassword());
        userEntityDTO.setCreatedAt(testimonialEntity.getUser().getCreatedAt());
        userEntityDTO.setUpdatedAt(testimonialEntity.getUser().getUpdatedAt());
        userEntityDTO.setRole(testimonialEntity.getUser().getRole());
        userEntityDTO.setVolunteer(testimonialEntity.getUser().getVolunteer());

        dto.setUserEntity(userEntityDTO);

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseCode("200");
        responseStatus.setResponseDesc("Testimonial fetched successfully");

        dto.setResponseStatus(responseStatus);

        return dto;
    }

    @Override
    public ResponseEntity<?> deleteTestimonial(TestimonialDTO testimonialDTO) {
        try {
            TestimonialEntity testimonialEntity = testimonialRepository.findById(testimonialDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Testimonial not found with id: " + testimonialDTO.getId(), new Throwable()));

            testimonialEntity.setId(testimonialDTO.getId());


            testimonialRepository.delete(testimonialEntity);

            TestimonialDTO testimonialSaved = new TestimonialDTO();
            testimonialSaved.setId(testimonialEntity.getId());
            testimonialSaved.setTestimonial(testimonialEntity.getTestimonial());
            testimonialSaved.setCreatedAt(testimonialEntity.getCreatedAt());
            testimonialSaved.setStatus(testimonialEntity.getStatus());


            if (testimonialEntity.getUser() != null) {
                testimonialSaved.setUserId(testimonialEntity.getUser().getId());
            }else{
                testimonialSaved.setUserId(null);
            }

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Testimonial deleted successfully");

            TestimonialResponseDTO testimonialResponseDTO = new TestimonialResponseDTO();
            testimonialResponseDTO.setTestimonialDTO(testimonialSaved);
            testimonialResponseDTO.setResponseStatus(responseStatus);

            return ResponseEntity.ok().body(testimonialResponseDTO);
        }catch (DataAccessException e) {
            logger.error("Database error while deleting testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while deleting testimonial: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }
}
