package com.volunteer.main.service.impl;


import com.volunteer.main.entity.OpportunityUserEntity;
import com.volunteer.main.entity.TimeSheetEntity;
import com.volunteer.main.model.request.TimeSheetDTO;
import com.volunteer.main.repositories.OpportunityUserRepository;
import com.volunteer.main.repositories.TimeSheetRepository;
import com.volunteer.main.service.TimeSheetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeSheetServiceImpl implements TimeSheetService {
    private final TimeSheetRepository timeSheetRepository;
    private final OpportunityUserRepository opportunityUserRepository;

    public TimeSheetServiceImpl(TimeSheetRepository timeSheetRepository, OpportunityUserRepository opportunityUserRepository) {
        this.timeSheetRepository = timeSheetRepository;
        this.opportunityUserRepository = opportunityUserRepository;
    }
    @Override
    public ResponseEntity<?> createTimeSheet(TimeSheetDTO requestDTO) {
        try {
            // Check if the user has the opportunity with status true
            OpportunityUserEntity opportunityUser = opportunityUserRepository.findByUserIdAndOpportunityId(requestDTO.getUserId(), requestDTO.getOpportunityId())
                    .orElseThrow(() -> new EntityNotFoundException("User does not have access to this opportunity"));

            TimeSheetEntity timesheet = new TimeSheetEntity();
            timesheet.setUser(opportunityUser.getUser());
            timesheet.setOpportunity(opportunityUser.getOpportunity());
            timesheet.setStatus(true); // Assuming initial status is true upon creation
            timesheet.setStartTime(requestDTO.getStartTime());
            timesheet.setEndTime(requestDTO.getEndTime());

            TimeSheetEntity savedTimesheet = timeSheetRepository.save(timesheet);
            return ResponseEntity.ok(savedTimesheet);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateTimeSheet(Long timesheetId, TimeSheetDTO requestDTO) {
        try {
            TimeSheetEntity timesheet = timeSheetRepository.findById(timesheetId)
                    .orElseThrow(() -> new EntityNotFoundException("Timesheet not found with id: " + timesheetId));

            timesheet.setStartTime(requestDTO.getStartTime());
            timesheet.setEndTime(requestDTO.getEndTime());

            TimeSheetEntity updatedTimesheet = timeSheetRepository.save(timesheet);
            return ResponseEntity.ok(updatedTimesheet);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteTimeSheet(Long timesheetId) {
        try {
            TimeSheetEntity timesheet = timeSheetRepository.findById(timesheetId)
                    .orElseThrow(() -> new EntityNotFoundException("Timesheet not found with id: " + timesheetId));

            timeSheetRepository.delete(timesheet);
            return ResponseEntity.ok("Timesheet deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getTimeSheetById(Long timesheetId) {
        try {
            TimeSheetEntity timesheet = timeSheetRepository.findById(timesheetId)
                    .orElseThrow(() -> new EntityNotFoundException("Timesheet not found with id: " + timesheetId));
            return ResponseEntity.ok(timesheet);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getTimeSheetsByUserId(Long userId) {
        try {
            List<TimeSheetEntity> timesheets = timeSheetRepository.findByUserId(userId);
            return ResponseEntity.ok(timesheets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> transitionTimeSheetStatus(Long timesheetId, Boolean newStatus) {
        try {
            TimeSheetEntity timesheet = timeSheetRepository.findById(timesheetId)
                    .orElseThrow(() -> new EntityNotFoundException("Timesheet not found with id: " + timesheetId));

            timesheet.setStatus(newStatus);

            TimeSheetEntity updatedTimesheet = timeSheetRepository.save(timesheet);
            return ResponseEntity.ok(updatedTimesheet);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllTimeSheets() {
        try {
            List<TimeSheetEntity> timeSheets = (List<TimeSheetEntity>) timeSheetRepository.findAll();
            return ResponseEntity.ok(timeSheets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getTimeSheetByCriteria(Long timesheetId, Long userId) {
        try {
            if (timesheetId != null && userId != null) {
                // Get timesheet by ID
                TimeSheetEntity timesheet = timeSheetRepository.findById(timesheetId)
                        .orElseThrow(() -> new EntityNotFoundException("Timesheet not found with id: " + timesheetId));
                return ResponseEntity.ok(timesheet);
            } else if (userId != null) {
                // Get timesheets by user ID
                List<TimeSheetEntity> timesheets = timeSheetRepository.findByUserId(userId);
                return ResponseEntity.ok(timesheets);
            } else {
                return ResponseEntity.badRequest().body("Either timesheetId or userId must be provided");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
