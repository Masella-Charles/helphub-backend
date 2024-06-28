package com.volunteer.main.controller;


import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.model.request.DisasterDTO;
import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.service.DisasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/disaster")
@RestController
public class DisasterController {
    private static final Logger logger = LoggerFactory.getLogger(DisasterController.class);

    private final DisasterService disasterService;

    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid DisasterDTO disasterDTO,
                                                     @PathVariable(name = "id", required = false) Long id,
                                                     @PathVariable(name = "status", required = false) Boolean status,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", disasterDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/disaster/create" -> createDisaster(disasterDTO);
            case "/api/v1/disaster/list" -> getAllDisasters();
            case "/api/v1/disaster/get" -> getDisasterByIdOrStatus(disasterDTO);
            case "/api/v1/disaster/update" -> updateDisaster(disasterDTO);
            case "/api/v1/disaster/transition" -> transitionDisaster(disasterDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createDisaster(DisasterDTO disasterDTO) {
        return disasterService.createDisaster(disasterDTO);
    }

    private ResponseEntity<?> getAllDisasters() {
        List<DisasterEntity> disasterEntities = disasterService.getAllDisasters();
        return ResponseEntity.ok(disasterEntities);
    }


    private ResponseEntity<?> getDisasterById(DisasterDTO disasterDTO) {
        return disasterService.getDisasterById(disasterDTO);
    }

    private ResponseEntity<?> getDisasterByIdOrStatus(DisasterDTO disasterDTO) {
        Long id = disasterDTO.getId();
        Boolean status = disasterDTO.getStatus();
        return disasterService.getDisasterByIdOrStatus(id,status);
    }

    private ResponseEntity<?> updateDisaster(DisasterDTO disasterDTO) {
        return disasterService.updateDisaster(disasterDTO);
    }

    private ResponseEntity<?> transitionDisaster(DisasterDTO disasterDTO) {
        return disasterService.transitionDisaster(disasterDTO);
    }
}
