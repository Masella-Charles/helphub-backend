package com.volunteer.main.service;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.model.request.DisasterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DisasterService {

    ResponseEntity<?> createDisaster(DisasterDTO disasterDTO);
    ResponseEntity<?> updateDisaster(DisasterDTO disasterDTO);
    ResponseEntity<?> transitionDisaster(DisasterDTO disasterDTO);
    ResponseEntity<?> getDisasterById(DisasterDTO disasterDTO);
    List<DisasterEntity> getAllDisasters();
}
