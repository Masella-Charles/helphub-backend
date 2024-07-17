package com.volunteer.main.controller;

import com.volunteer.main.model.request.OpportunityUserDTO;
import com.volunteer.main.model.response.ResponseStatus;
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

@RequestMapping("api/v1/opportunityUser")
@RestController
@CrossOrigin(origins = "*")
public class OpportunityUserController {
    private static final Logger logger = LoggerFactory.getLogger(OpportunityUserController.class);

    private final OpportunityService opportunityService;

    public OpportunityUserController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }


    @RequestMapping(value = {"/volunteerNow","/volunteerTransition","/getOpportunityUser",
            "listOpportunityUser","/delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid OpportunityUserDTO opportunityUserDTO,
                                                     @RequestPart(required = false, name="opportunityImage") MultipartFile opportunityImage,
                                                     @RequestParam(required = false) Long id,
                                                     @RequestParam(required = false) Boolean status,
                                                     @RequestParam(required = false) Long userId,
                                                     @RequestParam(required = false) Long opportunityId,
                                                     @RequestParam(required = false) Long disasterId,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", opportunityUserDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/opportunityUser/volunteerNow" -> volunteerNow(opportunityUserDTO);
            case "/api/v1/opportunityUser/volunteerTransition" -> volunteerTransition(opportunityUserDTO);
            case "/api/v1/opportunityUser/getOpportunityUser" -> getOpportunityUser(opportunityUserDTO);
            case "/api/v1/opportunityUser/listOpportunityUser" -> getAllOpportunityUsers();
            case "/api/v1/opportunityUser/delete" -> deleteOpportunityUser(opportunityUserDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }


    public ResponseEntity<?> volunteerNow(OpportunityUserDTO opportunityUserDTO) {
        Long userId = opportunityUserDTO.getUserId();
        Long opportunityId = opportunityUserDTO.getOpportunityId();
        logger.info("Volunteer Now request: userId={}, opportunityId={}", userId, opportunityId);
        OpportunityUserDTO response = opportunityService.volunteerNow(userId, opportunityId);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> volunteerTransition(OpportunityUserDTO opportunityUserDTO) {
        Long opportunityUserId = opportunityUserDTO.getId();
        Boolean newStatus = opportunityUserDTO.getStatus();
        OpportunityUserDTO response = opportunityService.volunteerTransition(opportunityUserId, newStatus);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getOpportunityUser(OpportunityUserDTO opportunityUserDTO) {
        Long id = opportunityUserDTO.getId();
        Long userId = opportunityUserDTO.getUserId();
        Long opportunityId = opportunityUserDTO.getOpportunityId();
        Boolean status = opportunityUserDTO.getStatus() != null ? opportunityUserDTO.getStatus() : false; // Defaulting to false if status is null
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

    public ResponseEntity<?> deleteOpportunityUser(OpportunityUserDTO opportunityUserDTO) {
        Long id = opportunityUserDTO.getId();
        opportunityService.deleteOpportunityUser(id);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseCode("200");
        responseStatus.setResponseDesc("Volunteer assignment deleted successfully");
        return ResponseEntity.ok(responseStatus);
    }

}
