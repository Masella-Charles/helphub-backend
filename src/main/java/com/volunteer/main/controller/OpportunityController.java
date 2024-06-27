package com.volunteer.main.controller;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.model.request.OpportunityUserDTO;
import com.volunteer.main.model.response.OpportunityDisasterResponseDTO;
import com.volunteer.main.service.DonationService;
import com.volunteer.main.service.OpportunityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("api/v1/opportunity")
@RestController
public class OpportunityController {
    private static final Logger logger = LoggerFactory.getLogger(OpportunityController.class);

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition,",
            "/volunteerNow","/volunteerTransition","/getOpportunityUser","listOpportunityUser"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid OpportunityDTO opportunityDTO,
                                                     @RequestPart(required = false, name="opportunityImage") MultipartFile opportunityImage,
                                                     @RequestParam(required = false) Long id,
                                                     @RequestParam(required = false) Boolean status,
                                                     @RequestParam(required = false) Long userId,
                                                     @RequestParam(required = false) Long opportunityId,
                                                     @RequestParam(required = false) Long disasterId,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", opportunityDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/opportunity/create" -> createOpportunity(opportunityDTO,opportunityImage);
            case "/api/v1/opportunity/list" -> getAllOpportunities();
            case "/api/v1/opportunity/get" -> getOpportunitiesByIdStatusOrDisasterId(id,status,-disasterId);
            case "/api/v1/opportunity/update" -> updateOpportunity(opportunityDTO,opportunityImage);
            case "/api/v1/opportunity/transition" -> transitionOpportunity(opportunityDTO);
            case "/api/v1/opportunity/volunteerNow" -> volunteerNow(userId,opportunityId);
            case "/api/v1/opportunity/volunteerTransition" -> volunteerTransition(userId,opportunityId,status);
            case "/api/v1/opportunity/getOpportunityUser" -> getOpportunityUser(id,status,userId,opportunityId);
            case "/api/v1/opportunity/listOpportunityUser" -> getAllOpportunityUsers();
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createOpportunity(OpportunityDTO opportunityDTO,MultipartFile opportunityImage) {
        return opportunityService.createOpportunity(opportunityDTO,opportunityImage);
    }


    private ResponseEntity<?> getAllOpportunities() {
        List<OpportunityEntity> opportunityEntities = opportunityService.getAllOpportunities();
        return ResponseEntity.ok(opportunityEntities);
    }


    private ResponseEntity<?> getOpportunitiesByIdStatusOrDisasterId( Long id, Boolean status, Long disasterId) {
        List<OpportunityDisasterResponseDTO> response = opportunityService.getOpportunitiesByIdStatusOrDisasterId(id, status, disasterId);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<?> updateOpportunity(OpportunityDTO opportunityDTO,MultipartFile opportunityImage) {
        return opportunityService.updateOpportunity(opportunityDTO,opportunityImage);
    }

    private ResponseEntity<?> transitionOpportunity(OpportunityDTO opportunityDTO) {
        return opportunityService.transitionOpportunity(opportunityDTO);
    }

    public ResponseEntity<?> volunteerNow(Long userId, Long opportunityId) {
        logger.info("Volunteer Now request: userId={}, opportunityId={}", userId, opportunityId);
        OpportunityUserDTO response = opportunityService.volunteerNow(userId, opportunityId);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> volunteerTransition(Long userId, Long opportunityId,Boolean status) {
        logger.info("Volunteer Transition request: userId={}, opportunityId={}, opportunityId={}", userId, opportunityId,status);
        OpportunityUserDTO response = opportunityService.volunteerTransition(userId, opportunityId,status);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getOpportunityUser(Long id, Boolean status,Long userId, Long opportunityId) {
        Object response = opportunityService.getOpportunityUserByIdOrStatusOrUserIdOrOpportunityId(id, status, userId, opportunityId);
        if (response instanceof List) {
            return ResponseEntity.ok((List<OpportunityUserDTO>) response);
        } else {
            return ResponseEntity.ok((OpportunityUserDTO) response);
        }
    }

    public ResponseEntity<?> getAllOpportunityUsers() {
        List<OpportunityUserDTO> response = opportunityService.getAllOpportunityUsers();
        return ResponseEntity.ok(response);
    }
}
