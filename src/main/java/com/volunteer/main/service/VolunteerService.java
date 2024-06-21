package com.volunteer.main.service;

import com.volunteer.main.entity.VolunteerEntity;
import com.volunteer.main.model.request.VolunteerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VolunteerService {
    ResponseEntity <?> createVolunteer(VolunteerDto volunteerDto);
    List<VolunteerEntity> listAllVolunteers();
    ResponseEntity <?> updateVolunteer(VolunteerDto volunteerDto);
    VolunteerEntity getVolunteerById(Long id);
}
