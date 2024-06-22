package com.volunteer.main.controller;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.OpportunityDTO;
import com.volunteer.main.service.DonationService;
import com.volunteer.main.service.OpportunityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/opportunity")
@RestController
public class OpportunityController {
    private static final Logger logger = LoggerFactory.getLogger(OpportunityController.class);

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid OpportunityDTO opportunityDTO,
                                                     @PathVariable(name = "id", required = false) Long id,
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
            case "/api/v1/opportunity/create" -> createOpportunity(opportunityDTO);
            case "/api/v1/opportunity/list" -> getAllOpportunities();
            case "/api/v1/opportunity/get" -> getOpportunityById(opportunityDTO);
            case "/api/v1/opportunity/update" -> updateOpportunity(opportunityDTO);
            case "/api/v1/opportunity/transition" -> transitionOpportunity(opportunityDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createOpportunity(OpportunityDTO opportunityDTO) {
        return opportunityService.createOpportunity(opportunityDTO);
    }

    private ResponseEntity<?> getAllOpportunities() {
        List<OpportunityEntity> opportunityEntities = opportunityService.getAllOpportunities();
        return ResponseEntity.ok(opportunityEntities);
    }


    private ResponseEntity<?> getOpportunityById(OpportunityDTO opportunityDTO) {
        return opportunityService.getOpportunityById(opportunityDTO);
    }

    private ResponseEntity<?> updateOpportunity(OpportunityDTO opportunityDTO) {
        return opportunityService.updateOpportunity(opportunityDTO);
    }

    private ResponseEntity<?> transitionOpportunity(OpportunityDTO opportunityDTO) {
        return opportunityService.transitionOpportunity(opportunityDTO);
    }
}
