package com.volunteer.main.controller;


import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.model.request.RoleDTO;
import com.volunteer.main.model.request.RolePermissionDTO;
import com.volunteer.main.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/role")
@RestController
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @RequestMapping(value = {"/create", "/list", "/get", "/update","/delete"},
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> handlePermissionRequest(HttpServletRequest httpServletRequest ,
                                                     @RequestBody(required = false) @Valid RoleDTO roleDTO,
                                                     @RequestParam(name = "id", required = false) Long id,
                                                     @RequestHeader HttpHeaders headers){

        logger.info("-------------------------------------------------------------------");
        logger.info("##### Request header ####: {} ", headers);
        logger.info("##### Request body ####: {} ", roleDTO);
        logger.info("-------------------------------------------------------------------");

        String path = httpServletRequest.getRequestURI();

        if (path == null) {
            // Handle missing path header
            return ResponseEntity.badRequest().body("Missing path header");
        }

        // Process based on the path
        return switch (path) {
            case "/api/v1/role/create" -> createRole(roleDTO);
            case "/api/v1/role/list" -> listAllRoles();
            case "/api/v1/role/get" -> getRoleById(roleDTO);
            case "/api/v1/role/update" -> updateRole(roleDTO);
            case "/api/v1/role/delete" -> deleteRoleById(roleDTO);
            default -> ResponseEntity.badRequest().body("Unsupported path: " + path);
        };

    }

    private ResponseEntity<?> createRole(RoleDTO roleDTO) {
        // Implement your logic to create a permission
        return roleService.createRole(roleDTO);
    }

    private ResponseEntity<?> listAllRoles() {
        // Implement your logic to list all permissions
        List<RoleEntity> roleEntities = roleService.listAllRoles();
        return ResponseEntity.ok(roleEntities);
    }

    private ResponseEntity<?> getRoleById(RoleDTO roleDTO) {
        Long id = roleDTO.getId();
        RoleEntity roleEntity = roleService.getRoleById(id);
        return ResponseEntity.ok(roleEntity);
    }

    private ResponseEntity<?> updateRole(RoleDTO roleDTO) {
        // Implement your logic to update a permission
        return roleService.updateRole(roleDTO);
    }

    private ResponseEntity<?> deleteRoleById(RoleDTO roleDTO) {
        Long id = roleDTO.getId();
        return roleService.deleteRoleById(id);
    }
}
