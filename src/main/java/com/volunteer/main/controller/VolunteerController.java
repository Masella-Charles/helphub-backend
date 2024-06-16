package com.volunteer.main.controller;

import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.entity.VolunteerEntity;
import com.volunteer.main.model.request.VolunteerDto;
import com.volunteer.main.service.VolunteerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/volunteer")
@RestController
public class VolunteerController {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerController.class);
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @RequestMapping(value = {"/create", "/list", "/get/{id}", "/update"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<?> handleVolunteerRequest(HttpServletRequest httpServletRequest ,
                                                    @RequestBody(required = false) @Valid VolunteerDto volunteerDto,
                                                    @PathVariable(required = false) Long id,
                                                    @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", volunteerDto);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }



        // Process based on the path
        return switch (path) {
            case "/api/v1/volunteer/create" -> createVolunteer(volunteerDto);
            case "/api/v1/volunteer/list" -> listVolunteers();
            case "/api/v1/volunteer/get/{id}" -> getVolunteerById(id, volunteerDto);
            case "/api/v1/volunteer/update" -> updateVolunteer(volunteerDto);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createVolunteer(VolunteerDto volunteerDto) {
        // Implement your logic to create a volunteer
        return volunteerService.createVolunteer(volunteerDto);
    }

    private ResponseEntity<?> listVolunteers() {
        // Implement your logic to list all volunteers
        List<VolunteerEntity> volunteers = volunteerService.listAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    private ResponseEntity<?> getVolunteerById(Long id, VolunteerDto volunteerDto) {
        if (volunteerDto != null && volunteerDto.getId() != null && !volunteerDto.getId().equals(id)) {
            // Handle mismatched IDs between path variable and request body
            return ResponseEntity.badRequest().body("Mismatched ID");
        }
        // Implement your logic to get a volunteer by ID
        VolunteerEntity volunteer = volunteerService.getVolunteerById(id);
        return ResponseEntity.ok(volunteer);
    }

    private ResponseEntity<?> updateVolunteer(VolunteerDto volunteerDto) {
        // Implement your logic to update a volunteer
        return volunteerService.updateVolunteer(volunteerDto);
    }
}
