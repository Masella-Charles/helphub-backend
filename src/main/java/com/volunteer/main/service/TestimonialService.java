package com.volunteer.main.service;

import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.TestimonialEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.TestimonialDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TestimonialService {
    ResponseEntity<?> createTestimonial(TestimonialDTO testimonialDTO);
    ResponseEntity<?> updateTestimonial(TestimonialDTO testimonialDTO);
    ResponseEntity<?> transitionTestimonial(TestimonialDTO testimonialDTO);
    ResponseEntity<?> getTestimonialById(TestimonialDTO testimonialDTO);
    List<TestimonialEntity> getAllTestimonials();
    ResponseEntity<?> deleteTestimonial(TestimonialDTO testimonialDTO);
    ResponseEntity<?> getTestimonialByCriteria(Long id, Boolean status, Long userId);
}
