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

@RequestMapping("api/v1/donation")
@RestController
public class DonationController {
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition",
            "/createDistribution", "/listDistribution", "/getDistribution", "/updateDistribution",
            "/deleteDistribution"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid DonationDTO donationDTO,
                                                     @RequestBody(required = false) @Valid DonationDistributionDTO donationDistributionDTO,
                                                     @RequestParam(name = "id", required = false) Long id,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", donationDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/donation/create" -> createDonation(donationDTO);
            case "/api/v1/donation/list" -> getAllDonations();
            case "/api/v1/donation/get" -> getDonationByIdOrStatusOrDisasterId(donationDTO);
            case "/api/v1/donation/update" -> updateDonation(donationDTO);
            case "/api/v1/donation/transition" -> transitionDonation(donationDTO);
            case "/api/v1/donation/createDistribution" -> createDonationDistribution(donationDistributionDTO);
            case "/api/v1/donation/listDistribution" -> getAllDonationDistributions();
            case "/api/v1/donation/getDistribution" -> getDonationDistributionById(id);
            case "/api/v1/donation/updateDistribution" -> updateDonationDistribution(id,donationDistributionDTO);
            case "/api/v1/donation/deleteDistribution" -> deleteDonationDistribution(id);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createDonation(DonationDTO donationDTO) {
        return donationService.createDonation(donationDTO);
    }

    private ResponseEntity<?> getAllDonations() {
        List<DonationEntity> donationEntities = donationService.getAllDonations();
        return ResponseEntity.ok(donationEntities);
    }


    private ResponseEntity<?> getDonationById(DonationDTO donationDTO) {
        return donationService.getDonationById(donationDTO);
    }

    private ResponseEntity<?> getDonationByIdOrStatusOrDisasterId(DonationDTO donationDTO) {
        return donationService.getDonationByIdOrStatusOrDisasterId(donationDTO);
    }

    private ResponseEntity<?> updateDonation(DonationDTO donationDTO) {
        return donationService.updateDonation(donationDTO);
    }

    private ResponseEntity<?> transitionDonation(DonationDTO donationDTO) {
        return donationService.transitionDonation(donationDTO);
    }

    public ResponseEntity<?> createDonationDistribution(DonationDistributionDTO donationDistributionDTO) {
        return donationService.createDonationDistribution(donationDistributionDTO);
    }

    public ResponseEntity<?> updateDonationDistribution( Long id, DonationDistributionDTO donationDistributionDTO) {
        return donationService.updateDonationDistribution(id, donationDistributionDTO);
    }

    public ResponseEntity<?> deleteDonationDistribution(Long id) {
        return donationService.deleteDonationDistribution(id);
    }

    public ResponseEntity<?> getDonationDistributionById(Long id) {
        return donationService.getDonationDistributionById(id);
    }

    public ResponseEntity<?> getAllDonationDistributions() {
        return donationService.getAllDonationDistributions();
    }
}
