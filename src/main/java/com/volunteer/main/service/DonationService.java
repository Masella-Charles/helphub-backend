package com.volunteer.main.service;


import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.model.request.DonationDTO;
import com.volunteer.main.model.request.DonationDistributionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DonationService {
    ResponseEntity<?> createDonation(DonationDTO donationDTO);
    ResponseEntity<?> updateDonation(DonationDTO donationDTO);
    ResponseEntity<?> transitionDonation(DonationDTO donationDTO);
    ResponseEntity<?> getDonationById(DonationDTO donationDTO);
    List<DonationEntity> getAllDonations();
    ResponseEntity<?> getDonationByIdOrStatusOrDisasterId(DonationDTO donationDTO);

    ResponseEntity<?> createDonationDistribution(DonationDistributionDTO donationDistributionDTO);
    ResponseEntity<?> updateDonationDistribution(Long id, DonationDistributionDTO donationDistributionDTO);
    ResponseEntity<?> deleteDonationDistribution(Long id);
    ResponseEntity<?> getDonationDistributionById(Long id);
    ResponseEntity<?> getAllDonationDistributions();
}
