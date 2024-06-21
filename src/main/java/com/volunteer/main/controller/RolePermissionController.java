package com.volunteer.main.controller;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.model.request.RolePermissionDTO;
import com.volunteer.main.service.PermissionService;
import com.volunteer.main.service.RolePermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequestMapping("api/v1/role-permission")
@RestController
public class RolePermissionController {
    private static final Logger logger = LoggerFactory.getLogger(RolePermissionController.class);
    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid RolePermissionDTO rolePermissionDTO,
                                                     @RequestParam(name = "roleId", required = false) Long roleId,
                                                     @RequestParam(name = "permissionId", required = false) Long permissionId,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", rolePermissionDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/role-permission/create" -> addPermissionToRole(roleId,permissionId);
            case "/api/v1/role-permission/get" -> getPermissionsByRole(roleId);
            case "/api/v1/role-permission/list" -> getAllRolePermissions();
            case "/api/v1/role-permission/update" -> editRolePermissions(roleId, Collections.singleton(permissionId));
            case "/api/v1/role-permission/delete" -> removePermissionFromRole(roleId,permissionId);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> addPermissionToRole(Long roleId, Long permissionId) {
        logger.info("Adding permission {} to role {}", permissionId, roleId);
        return rolePermissionService.addPermissionToRole(roleId, permissionId);

    }

    private ResponseEntity<?> getAllRolePermissions() {
        logger.info("Fetching all role-permission mappings");
        return rolePermissionService.getAllRolePermissions();
    }

    private ResponseEntity<?> getPermissionsByRole(Long roleId) {
        logger.info("Fetching permissions for role {}", roleId);
        return rolePermissionService.getPermissionsByRole(roleId);
    }

    private ResponseEntity<?> editRolePermissions(Long roleId, Set<Long> permissionId) {
        return rolePermissionService.editRolePermissions(roleId, permissionId);
    }

    private ResponseEntity<?> removePermissionFromRole(Long roleId, Long permissionId) {
        logger.info("Removing permission {} from role {}", permissionId, roleId);
        return rolePermissionService.removePermissionFromRole(roleId, permissionId);

    }
}
