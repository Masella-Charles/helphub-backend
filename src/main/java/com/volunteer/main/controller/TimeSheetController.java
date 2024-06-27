package com.volunteer.main.controller;

import com.volunteer.main.model.request.DisasterDTO;
import com.volunteer.main.model.request.TimeSheetDTO;
import com.volunteer.main.service.DisasterService;
import com.volunteer.main.service.TimeSheetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timesheet")
public class TimeSheetController {
    private final TimeSheetService timeSheetService;

    public TimeSheetController(TimeSheetService timeSheetService) {
        this.timeSheetService = timeSheetService;
    }

    private static final Logger logger = LoggerFactory.getLogger(TimeSheetController.class);


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/transition","delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid TimeSheetDTO timeSheetDTO,
                                                     @RequestParam (required = false) Long userId,
                                                     @RequestParam (required = false) Long timeSheetId,
                                                     @RequestParam (required = false) Boolean status,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", timeSheetDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/timesheet/create" -> createTimeSheet(timeSheetDTO);
            case "/api/v1/timesheet/list" -> getAllTimeSheets();
            case "/api/v1/timesheet/get" -> getTimeSheetByCriteria(timeSheetId,userId);
            case "/api/v1/timesheet/update" -> updateTimeSheet(timeSheetId,timeSheetDTO);
            case "/api/v1/timesheet/transition" -> transitionTimeSheetStatus(timeSheetId,status);
            case "/api/v1/timesheet/delete" -> deleteTimeSheet(timeSheetId);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    public ResponseEntity<?> createTimeSheet(TimeSheetDTO requestDTO) {
        return timeSheetService.createTimeSheet(requestDTO);
    }

    public ResponseEntity<?> updateTimeSheet(Long timesheetId,TimeSheetDTO requestDTO) {
        return timeSheetService.updateTimeSheet(timesheetId, requestDTO);
    }


    public ResponseEntity<?> deleteTimeSheet(Long timesheetId) {
        return timeSheetService.deleteTimeSheet(timesheetId);
    }

    public ResponseEntity<?> getTimeSheetById(Long timesheetId) {
        return timeSheetService.getTimeSheetById(timesheetId);
    }


    public ResponseEntity<?> getTimeSheetsByUserId(Long userId) {
        return timeSheetService.getTimeSheetsByUserId(userId);
    }

    public ResponseEntity<?> getAllTimeSheets() {
        return timeSheetService.getAllTimeSheets();
    }

    public ResponseEntity<?> getTimeSheetByCriteria(Long timeSheetId,Long userId) {
        return timeSheetService.getTimeSheetByCriteria(timeSheetId, userId);
    }


    public ResponseEntity<?> transitionTimeSheetStatus(Long timesheetId, Boolean status) {
        return timeSheetService.transitionTimeSheetStatus(timesheetId, status);
    }
}
