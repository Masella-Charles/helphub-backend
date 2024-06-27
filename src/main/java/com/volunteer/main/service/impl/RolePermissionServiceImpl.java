package com.volunteer.main.service.impl;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.RolePermissionDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.repositories.PermissionRepository;
import com.volunteer.main.repositories.RoleRepository;
import com.volunteer.main.service.RolePermissionService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private static final Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public RolePermissionServiceImpl(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public ResponseEntity<?> addPermissionToRole(Long roleId, Long permissionId) {
        try {
            RoleEntity role = roleRepository.findById(roleId).
                    orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
            PermissionEntity permission = permissionRepository.findById(permissionId).
                    orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));

            role.getPermissionEntities().add(permission);
            roleRepository.save(role);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Permission added to role successfully.");
            return ResponseEntity.ok().body(responseStatus);
        } catch (Exception e) {
            logger.error("Error adding permission to role: {}", e.getMessage());
            throw new CustomAuthenticationException("Error adding permission to role: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> editRolePermissions(Long roleId, Set<Long> permissionIds) {
        try {
            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));

            Set<PermissionEntity> newPermissions = new HashSet<>();
            for (Long permissionId : permissionIds) {
                PermissionEntity permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));
                newPermissions.add(permission);
            }

            role.setPermissionEntities(newPermissions);
            roleRepository.save(role);

            RolePermissionDTO updatedRolePermissions = new RolePermissionDTO(
                    role.getId(),
                    role.getRoleName(),
                    role.getPermissionEntities()
            );

            return ResponseEntity.ok().body(updatedRolePermissions);
        } catch (CustomAuthenticationException e) {
            logger.error("Error updating role permissions: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating role permissions: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error updating role permissions: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> removePermissionFromRole(Long roleId, Long permissionId) {
        try {
            RoleEntity role = roleRepository.findById(roleId).
                    orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
            PermissionEntity permission = permissionRepository.findById(permissionId).
                    orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));

            role.getPermissionEntities().remove(permission);
            roleRepository.save(role);

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Permission removed from role successfully.");
            return ResponseEntity.ok().body(responseStatus);
        } catch (Exception e) {
            logger.error("Error removing permission from role: {}", e.getMessage());
            throw new CustomAuthenticationException("Error removing permission from role: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getPermissionsByRole(Long roleId) {
        try {
            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
            Set<PermissionEntity> permissions = role.getPermissionEntities();

            RolePermissionDTO rolePermissionDTO = new RolePermissionDTO(
                    role.getId(),
                    role.getRoleName(),
                    permissions
            );

            return ResponseEntity.ok().body(rolePermissionDTO);
        } catch (Exception e) {
            logger.error("Error fetching permissions for role: {}", e.getMessage());
            throw new CustomAuthenticationException("Error fetching permissions for role: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> getAllRolePermissions() {
        try {
            //List<RoleEntity> roles = (List<RoleEntity>) roleRepository.findAll();
            List<RoleEntity> roles = roleRepository.findAllWithPermissions();
            logger.info("List of roles: {}", roles);
            List<RolePermissionDTO> rolePermissions = roles.stream()
                    .map(role -> new RolePermissionDTO(role.getId(), role.getRoleName(), role.getPermissionEntities()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(rolePermissions);
        } catch (Exception e) {
            logger.error("Error fetching all role permissions: {}", e.getMessage());
            throw new CustomAuthenticationException("Error fetching all role permissions: " + e.getMessage(), e);
        }
    }
}
