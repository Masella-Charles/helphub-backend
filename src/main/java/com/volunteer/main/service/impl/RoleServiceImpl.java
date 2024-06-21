package com.volunteer.main.service.impl;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.model.request.RoleDTO;
import com.volunteer.main.model.response.PermissionResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.model.response.RoleResponseDTO;
import com.volunteer.main.repositories.PermissionRepository;
import com.volunteer.main.repositories.RoleRepository;
import com.volunteer.main.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<?> createRole(RoleDTO roleDTO) {
        try {
            // Create and populate the entity from the DTO
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRoleName(roleDTO.getRoleName());
            roleEntity.setRoleDescription(roleDTO.getRoleDescription());

            // Save the entity to the database
            roleEntity = roleRepository.save(roleEntity);

            // Convert the saved entity back to DTO
            RoleDTO savedRoleDTO = new RoleDTO();
            savedRoleDTO.setId(roleEntity.getTId());
            savedRoleDTO.setRoleName(roleEntity.getRoleName());
            savedRoleDTO.setRoleDescription(roleEntity.getRoleDescription());

            // Log the saved entity
            logger.info("Role created: {}", savedRoleDTO);

            // Create response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Role created successfully.");

            // Create response DTO
            RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
            roleResponseDTO.setRoleDTO(savedRoleDTO);
            roleResponseDTO.setResponseStatus(responseStatus);

            // Return the response entity
            return ResponseEntity.ok().body(roleResponseDTO);
        } catch (DataIntegrityViolationException e) {
            logger.error("Database error while creating role: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating role: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RoleEntity> listAllRoles() {
        try {
            Iterable<RoleEntity> roleIterable = roleRepository.findAll();
            return StreamSupport.stream(roleIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while listing all roles: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all roles: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> updateRole(RoleDTO roleDTO) {
        try {
            // Retrieve the existing role entity from the database
            RoleEntity existingRole = roleRepository.findById(roleDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Role not found with id: " + roleDTO.getId(), new Throwable()));

            // Update the existing role entity with the new data from roleDTO
            existingRole.setRoleName(roleDTO.getRoleName());
            existingRole.setRoleDescription(roleDTO.getRoleDescription());

            // Save the updated role entity
            existingRole = roleRepository.save(existingRole);

            // Convert the updated entity to DTO
            RoleDTO updatedRoleDTO = new RoleDTO();
            updatedRoleDTO.setId(existingRole.getTId());
            updatedRoleDTO.setRoleName(existingRole.getRoleName());
            updatedRoleDTO.setRoleDescription(existingRole.getRoleDescription());

            // Log the updated role
            logger.info("Role updated: {}", updatedRoleDTO);

            // Create response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Role updated successfully.");

            // Create response DTO
            RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
            roleResponseDTO.setRoleDTO(updatedRoleDTO);
            roleResponseDTO.setResponseStatus(responseStatus);

            // Return the response entity
            return ResponseEntity.ok().body(roleResponseDTO);
        } catch (CustomAuthenticationException e) {
            logger.error("Error while updating role: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while updating role: {}", e.getMessage());
            throw new CustomAuthenticationException("Failed to update role: " + e.getMessage(), e);
        }
    }

    @Override
    public RoleEntity getRoleById(Long id) {
        try {
            // Retrieve the role entity from the database by ID
            return roleRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        } catch (EntityNotFoundException e) {
            // Log the error or rethrow to let the caller handle it
            throw e;
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            // Rethrow a more generic exception or handle as appropriate
            throw new CustomAuthenticationException("Failed to get role: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> deleteRoleById(Long id) {
        try {
            // Retrieve the permission entity from the database by ID
            Optional<RoleEntity> optionalRole = Optional.ofNullable(roleRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id)));

            // Role found, delete it from the repository
            RoleEntity roleToDelete = optionalRole.get();
            roleRepository.delete(roleToDelete);

            // Construct the response DTO
            RoleResponseDTO responseDTO = new RoleResponseDTO();

            // Construct the response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Role deleted successfully.");
            responseDTO.setResponseStatus(responseStatus);

            // Construct the roleDTO
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(roleToDelete.getTId());
            roleDTO.setRoleName(roleToDelete.getRoleName());
            roleDTO.setRoleDescription(roleToDelete.getRoleDescription());
            responseDTO.setRoleDTO(roleDTO);

            // Return the response entity with the DTO
            return ResponseEntity.ok().body(responseDTO);
        } catch (CustomAuthenticationException e) {
            logger.error("Error while deleting role: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while deleting role: {}", e.getMessage());
            throw new CustomAuthenticationException("Failed to delete role: " + e.getMessage(), e);
        }
    }
}
