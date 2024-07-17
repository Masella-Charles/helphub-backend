package com.volunteer.main.service.impl;


import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.entity.OpportunityUserEntity;
import com.volunteer.main.entity.TimeSheetEntity;
import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.TimeSheetDTO;
import com.volunteer.main.model.response.TimeSheetResponseDTO;
import com.volunteer.main.repositories.OpportunityRepository;
import com.volunteer.main.repositories.OpportunityUserRepository;
import com.volunteer.main.repositories.TimeSheetRepository;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.TimeSheetService;
import com.volunteer.main.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimeSheetServiceImpl implements TimeSheetService {
    private static final Logger log = LoggerFactory.getLogger(TimeSheetServiceImpl.class);
    private final TimeSheetRepository timeSheetRepository;
    private final OpportunityUserRepository opportunityUserRepository;
    private final Utils utils;

    private final UserRepository userRepository;

    private final OpportunityRepository opportunityRepository;

    public TimeSheetServiceImpl(TimeSheetRepository timeSheetRepository, OpportunityUserRepository opportunityUserRepository, Utils utils, UserRepository userRepository, OpportunityRepository opportunityRepository) {
        this.timeSheetRepository = timeSheetRepository;
        this.opportunityUserRepository = opportunityUserRepository;
        this.utils = utils;
        this.userRepository = userRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public ResponseEntity<?> createTimeSheet(TimeSheetDTO requestDTO) {
        try {
            // Fetch User and Opportunity entities
            UserEntity user = userRepository.findById(Math.toIntExact(requestDTO.getUserId()))
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            OpportunityEntity opportunity = opportunityRepository.findById(requestDTO.getOpportunityId())
                    .orElseThrow(() -> new EntityNotFoundException("Opportunity not found"));

            TimeSheetEntity timesheet = new TimeSheetEntity();
            timesheet.setUser(user);
            timesheet.setOpportunity(opportunity);
            timesheet.setStatus(false); // Assuming initial status is false upon creation
            timesheet.setStartTime(requestDTO.getStartTime());
            timesheet.setEndTime(requestDTO.getEndTime());
            timesheet.setCreatedAt(LocalDate.now()); // Assuming utils.date() returns the current date
            timesheet.setUpdatedAt(null);

            TimeSheetEntity savedTimesheet = timeSheetRepository.save(timesheet);

            TimeSheetDTO timeSheetDTO = new TimeSheetDTO();
            timeSheetDTO.setId(savedTimesheet.getId());
            timeSheetDTO.setStartTime(savedTimesheet.getStartTime());
            timeSheetDTO.setEndTime(savedTimesheet.getEndTime());
            timeSheetDTO.setOpportunityId(savedTimesheet.getOpportunity().getId());
            timeSheetDTO.setStatus(savedTimesheet.getStatus());
            timeSheetDTO.setCreatedAt(savedTimesheet.getCreatedAt());
            timeSheetDTO.setUpdatedAt(savedTimesheet.getUpdatedAt());
            timeSheetDTO.setUserId(savedTimesheet.getUser().getId());


            return ResponseEntity.ok(timeSheetDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateTimeSheet(TimeSheetDTO updateDTO) {
        try {
            TimeSheetEntity timeSheet = timeSheetRepository.findById(updateDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Timesheet not found"));

            if (!timeSheet.getUser().getId().equals(updateDTO.getUserId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID mismatch");
            }

            if (!timeSheet.getOpportunity().getId().equals(updateDTO.getOpportunityId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opportunity ID mismatch");
            }

            timeSheet.setStartTime(updateDTO.getStartTime());
            timeSheet.setEndTime(updateDTO.getEndTime());
            timeSheet.setStatus(updateDTO.getStatus());
            timeSheet.setUpdatedAt(LocalDate.now());

            TimeSheetEntity updatedTimeSheet = timeSheetRepository.save(timeSheet);

            TimeSheetDTO timeSheetDTO = new TimeSheetDTO();
            timeSheetDTO.setId(updatedTimeSheet.getId());
            timeSheetDTO.setStartTime(updatedTimeSheet.getStartTime());
            timeSheetDTO.setEndTime(updatedTimeSheet.getEndTime());
            timeSheetDTO.setOpportunityId(updatedTimeSheet.getOpportunity().getId());
            timeSheetDTO.setStatus(updatedTimeSheet.getStatus());
            timeSheetDTO.setCreatedAt(updatedTimeSheet.getCreatedAt());
            timeSheetDTO.setUpdatedAt(updatedTimeSheet.getUpdatedAt());
            timeSheetDTO.setUserId(updatedTimeSheet.getUser().getId());

            return ResponseEntity.ok(timeSheetDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteTimeSheet(Long timesheetId) {
        Map<String, String> response = new HashMap<>();
        try {
            TimeSheetEntity timesheet = timeSheetRepository.findById(timesheetId)
                    .orElseThrow(() -> new EntityNotFoundException("Timesheet not found with id: " + timesheetId));

            timeSheetRepository.delete(timesheet);
            response.put("status", "success");
            response.put("message", "Timesheet deleted successfully");
            return ResponseEntity.ok(response);
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
            List<TimeSheetEntity> timeSheets = (List<TimeSheetEntity>) timeSheetRepository.findAll();

            // Filter time sheets by userId
            List<TimeSheetEntity> filteredTimeSheets = timeSheets.stream()
                    .filter(timesheet -> timesheet.getUser().getId().equals(userId))
                    .collect(Collectors.toList());

            List<TimeSheetResponseDTO> timeSheetSummaries = filteredTimeSheets.stream().map(timesheet -> {
                TimeSheetResponseDTO summary = new TimeSheetResponseDTO();
                summary.setId(timesheet.getId());
                summary.setUserId(timesheet.getUser().getId());
                summary.setUserName(timesheet.getUser().getFullName());
                summary.setOpportunityId(timesheet.getOpportunity().getId());
                summary.setOpportunityName(timesheet.getOpportunity().getName());
                summary.setStartTime(timesheet.getStartTime());
                summary.setEndTime(timesheet.getEndTime());
                summary.setStatus(timesheet.getStatus());
                summary.setCreatedAt(timesheet.getCreatedAt());
                summary.setUpdatedAt(timesheet.getUpdatedAt());
                return summary;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(timeSheetSummaries);
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

    public ResponseEntity<?> getAllTimeSheets() {
        try {
            List<TimeSheetEntity> timeSheets = (List<TimeSheetEntity>) timeSheetRepository.findAll();
            List<TimeSheetResponseDTO> timeSheetSummaries = timeSheets.stream().map(timesheet -> {
                TimeSheetResponseDTO summary = new TimeSheetResponseDTO();
                summary.setId(timesheet.getId());
                summary.setUserId(timesheet.getUser().getId());
                summary.setUserName(timesheet.getUser().getFullName());
                summary.setOpportunityId(timesheet.getOpportunity().getId());
                summary.setOpportunityName(timesheet.getOpportunity().getName());
                summary.setStartTime(timesheet.getStartTime());
                summary.setEndTime(timesheet.getEndTime());
                summary.setStatus(timesheet.getStatus());
                summary.setCreatedAt(timesheet.getCreatedAt());
                summary.setUpdatedAt(timesheet.getUpdatedAt());
                return summary;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(timeSheetSummaries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getTimeSheetByCriteria(Long timesheetId, Long userId) {
        try {
            List<TimeSheetEntity> timeSheets;

            if (userId != null && timesheetId != null) {
                timeSheets = timeSheetRepository.findByIdAndUserId(timesheetId, userId);
            } else if (userId != null) {
                timeSheets = timeSheetRepository.findByUserId(userId);
            } else if (timesheetId != null) {
                timeSheets = timeSheetRepository.findById(timesheetId)
                        .map(List::of) // Convert Optional<TimeSheetEntity> to List<TimeSheetEntity>
                        .orElseThrow(() -> new EntityNotFoundException("Timesheet not found"));
            } else {
                // Handle case where neither userId nor timesheetId is provided
                return ResponseEntity.badRequest().body("Please provide userId or timesheetId");
            }

            List<TimeSheetResponseDTO> timeSheetSummaries = timeSheets.stream().map(timesheet -> {
                TimeSheetResponseDTO summary = new TimeSheetResponseDTO();
                summary.setId(timesheet.getId());
                summary.setUserId(timesheet.getUser().getId());
                summary.setUserName(timesheet.getUser().getFullName());
                summary.setOpportunityId(timesheet.getOpportunity().getId());
                summary.setOpportunityName(timesheet.getOpportunity().getName());
                summary.setStartTime(timesheet.getStartTime());
                summary.setEndTime(timesheet.getEndTime());
                summary.setStatus(timesheet.getStatus());
                summary.setCreatedAt(timesheet.getCreatedAt());
                summary.setUpdatedAt(timesheet.getUpdatedAt());
                return summary;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(timeSheetSummaries);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
