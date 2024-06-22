package com.volunteer.main.service;


import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.model.request.DisasterDTO;
import com.volunteer.main.model.request.DonationDTO;
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
}
