package com.volunteer.main.controller;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.DonationDistributionDTO;
import com.volunteer.main.service.DonationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/donation-distribution")
@RestController
public class DonationDistributionController {
    private static final Logger logger = LoggerFactory.getLogger(DonationDistributionController.class);

    private final DonationService donationService;

    public DonationDistributionController(DonationService donationService) {
        this.donationService = donationService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid DonationDistributionDTO donationDistributionDTO,
                                                     @RequestParam(name = "id", required = false) Long id,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", donationDistributionDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/donation-distribution/create" -> createDonationDistribution(donationDistributionDTO);
            case "/api/v1/donation-distribution/list" -> getAllDonationDistributions();
            case "/api/v1/donation-distribution/get" -> getDonationDistributionById(donationDistributionDTO);
            case "/api/v1/donation-distribution/update" -> updateDonationDistribution(donationDistributionDTO);
            case "/api/v1/donation-distribution/delete" -> deleteDonationDistribution(donationDistributionDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    public ResponseEntity<?> createDonationDistribution(DonationDistributionDTO donationDistributionDTO) {
        return donationService.createDonationDistribution(donationDistributionDTO);
    }

    public ResponseEntity<?> updateDonationDistribution(DonationDistributionDTO donationDistributionDTO) {
        Long id = donationDistributionDTO.getId();
        return donationService.updateDonationDistribution(id, donationDistributionDTO);
    }

    public ResponseEntity<?> deleteDonationDistribution(DonationDistributionDTO donationDistributionDTO) {
        Long id = donationDistributionDTO.getId();
        return donationService.deleteDonationDistribution(id);
    }

    public ResponseEntity<?> getDonationDistributionById(DonationDistributionDTO donationDistributionDTO) {
        Long id = donationDistributionDTO.getId();
        return donationService.getDonationDistributionById(id);
    }

    public ResponseEntity<?> getAllDonationDistributions() {
        return donationService.getAllDonationDistributions();
    }
}


