package com.volunteer.main.controller;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("api/v1/permission")
@RestController
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    private final PermissionService permissionservice;

    public PermissionController(PermissionService permissionService) {
        this.permissionservice = permissionService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                    @RequestBody(required = false) @Valid PermissionDTO permissionDTO,
                                                     @RequestParam(name = "id", required = false) Long id,
                                                    @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", permissionDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/permission/create" -> createPermission(permissionDTO);
            case "/api/v1/permission/list" -> listAllPermissions();
            case "/api/v1/permission/get" -> getPermissionById(id, permissionDTO);
            case "/api/v1/permission/update" -> updatePermission(permissionDTO);
            case "/api/v1/permission/delete" -> deletePermissionById(id, permissionDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createPermission(PermissionDTO permissionDTO) {
        // Implement your logic to create a permission
        return permissionservice.createPermission(permissionDTO);
    }

    private ResponseEntity<?> listAllPermissions() {
        // Implement your logic to list all permissions
        List<PermissionEntity> permissionEntities = permissionservice.listAllPermissions();
        return ResponseEntity.ok(permissionEntities);
    }


    private ResponseEntity<?> getPermissionById(Long id, PermissionDTO permissionDTO) {
        if (permissionDTO != null && permissionDTO.getId() != null && !permissionDTO.getId().equals(id)) {
            // Handle mismatched IDs between path variable and request body
            return ResponseEntity.badRequest().body("Mismatched ID");
        }
        // Implement your logic to get a permission by ID
        PermissionEntity permissionEntity = permissionservice.getPermissionById(id);
        return ResponseEntity.ok(permissionEntity);
    }

    private ResponseEntity<?> updatePermission(PermissionDTO permissionDTO) {
        // Implement your logic to update a permission
        return permissionservice.updatePermission(permissionDTO);
    }

    private ResponseEntity<?> deletePermissionById(Long id, PermissionDTO permissionDTO) {
        if (permissionDTO != null && permissionDTO.getId() != null && !permissionDTO.getId().equals(id)) {
            // Handle mismatched IDs between path variable and request body
            return ResponseEntity.badRequest().body("Mismatched ID");
        }
        return permissionservice.deletePermissionById(id);
    }
}
