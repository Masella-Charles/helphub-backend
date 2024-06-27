package com.volunteer.main.service;

import com.volunteer.main.model.request.TimeSheetDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface TimeSheetService {
    ResponseEntity<?> createTimeSheet(TimeSheetDTO timeSheetDTO);

    ResponseEntity<?> updateTimeSheet(Long timesheetId, TimeSheetDTO timeSheetDTO);

    ResponseEntity<?> deleteTimeSheet(Long timesheetId);

    ResponseEntity<?> getTimeSheetById(Long timesheetId);

    ResponseEntity<?> getTimeSheetsByUserId(Long userId);

    ResponseEntity<?> getTimeSheetByCriteria(Long timesheetId, Long userId);

    ResponseEntity<?> transitionTimeSheetStatus(Long timesheetId, Boolean status);

    ResponseEntity<?> getAllTimeSheets();
}
