package com.volunteer.main.controller;


import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.model.request.ContactUsRequestDTO;
import com.volunteer.main.model.request.DisasterDTO;
import com.volunteer.main.model.response.ContactUsResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.service.ContactUsService;
import com.volunteer.main.service.DisasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/contactus")
@RestController
public class ContactUsController {
    private static final Logger logger = LoggerFactory.getLogger(DisasterController.class);

    private final ContactUsService contactUsService;

    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition","delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid ContactUsRequestDTO contactUsRequestDTO,
                                                     @PathVariable(name = "id", required = false) Long id,
                                                     @PathVariable(name = "status", required = false) Boolean status,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", contactUsRequestDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/contactus/create" -> createContactUs(contactUsRequestDTO);
            case "/api/v1/contactus/list" -> getAllContactUs();
            case "/api/v1/contactus/get" -> getContactUsById(contactUsRequestDTO);
            case "/api/v1/contactus/update" -> updateContactUs(contactUsRequestDTO);
            case "/api/v1/contactus/delete" -> deleteContactUs(contactUsRequestDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    public ResponseEntity<ContactUsResponseDTO> createContactUs(ContactUsRequestDTO contactUsRequestDTO) {
        ContactUsResponseDTO responseDTO = contactUsService.createContactUs(contactUsRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    public ResponseEntity<ContactUsResponseDTO> getContactUsById(ContactUsRequestDTO contactUsRequestDTO) {
        Long id = contactUsRequestDTO.getId();
        ContactUsResponseDTO responseDTO = contactUsService.getContactUsById(id);
        return ResponseEntity.ok(responseDTO);
    }


    public ResponseEntity<List<ContactUsResponseDTO>> getAllContactUs() {
        List<ContactUsResponseDTO> responseDTOList = contactUsService.getAllContactUs();
        return ResponseEntity.ok(responseDTOList);
    }


    public ResponseEntity<ContactUsResponseDTO> updateContactUs(ContactUsRequestDTO contactUsRequestDTO) {
        Long id = contactUsRequestDTO.getId();
        ContactUsResponseDTO responseDTO = contactUsService.updateContactUs(id, contactUsRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<?> deleteContactUs(ContactUsRequestDTO contactUsRequestDTO) {
        Long id = contactUsRequestDTO.getId();
        contactUsService.deleteContactUs(id);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseCode("200");
        responseStatus.setResponseDesc("Deleted successfully");
        return ResponseEntity.ok(responseStatus);
    }
}


