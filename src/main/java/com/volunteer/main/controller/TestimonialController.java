package com.volunteer.main.controller;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.TestimonialEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.TestimonialDTO;
import com.volunteer.main.service.DonationService;
import com.volunteer.main.service.TestimonialService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/testimonial")
@RestController
public class TestimonialController {
    private static final Logger logger = LoggerFactory.getLogger(TestimonialController.class);

    private final TestimonialService testimonialService;

    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition","/delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid TestimonialDTO testimonialDTO,
                                                     @RequestParam(value = "id", required = false) Long id,
                                                     @RequestParam(value = "status", required = false) Boolean status,
                                                     @RequestParam(value = "userId", required = false) Long userId,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", testimonialDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/testimonial/create" -> createTestimonial(testimonialDTO);
            case "/api/v1/testimonial/list" -> getAllTestimonials();
            case "/api/v1/testimonial/get" -> getTestimonialByCriteria(id, status, userId);
            case "/api/v1/testimonial/update" -> updateTestimonial(testimonialDTO);
            case "/api/v1/testimonial/transition" -> transitionTestimonial(testimonialDTO);
            case "/api/v1/testimonial/delete" -> deleteTestimonial(testimonialDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createTestimonial(TestimonialDTO testimonialDTO) {
        return testimonialService.createTestimonial(testimonialDTO);
    }

    private ResponseEntity<?> getAllTestimonials() {
        List<TestimonialEntity> testimonialEntities = testimonialService.getAllTestimonials();
        return ResponseEntity.ok(testimonialEntities);
    }


    private ResponseEntity<?> getTestimonialById(TestimonialDTO testimonialDTO) {
        return testimonialService.getTestimonialById(testimonialDTO);
    }

    public ResponseEntity<?> getTestimonialByCriteria( Long id,Boolean status, Long userId) {
        return testimonialService.getTestimonialByCriteria(id, status, userId);
    }

    private ResponseEntity<?> updateTestimonial(TestimonialDTO testimonialDTO) {
        return testimonialService.updateTestimonial(testimonialDTO);
    }

    private ResponseEntity<?> transitionTestimonial(TestimonialDTO testimonialDTO) {
        return testimonialService.transitionTestimonial(testimonialDTO);
    }

    private ResponseEntity<?> deleteTestimonial(TestimonialDTO testimonialDTO) {
        return testimonialService.deleteTestimonial(testimonialDTO);
    }
}
