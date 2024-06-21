package com.volunteer.main.service.impl;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.exceptions.CustomAuthenticationException;
import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.model.response.PermissionResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.repositories.PermissionRepository;
import com.volunteer.main.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public ResponseEntity<?> createPermission(PermissionDTO permissionDTO) {
        try {
            // Create and populate the entity from the DTO
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setPermissionName(permissionDTO.getPermissionName());
            permissionEntity.setPermissionDescription(permissionDTO.getPermissionDescription());

            // Save the entity to the database
            permissionEntity = permissionRepository.save(permissionEntity);

            // Convert the saved entity back to DTO
            PermissionDTO savedPermissionDTO = new PermissionDTO();
            savedPermissionDTO.setId(permissionEntity.getTId());
            savedPermissionDTO.setPermissionName(permissionEntity.getPermissionName());
            savedPermissionDTO.setPermissionDescription(permissionEntity.getPermissionDescription());

            // Log the saved entity
            logger.info("Permission created: {}", savedPermissionDTO);

            // Create response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Permission created successfully.");

            // Create response DTO
            PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();
            permissionResponseDTO.setPermissionDTO(savedPermissionDTO);
            permissionResponseDTO.setResponseStatus(responseStatus);

            // Return the response entity
            return ResponseEntity.ok().body(permissionResponseDTO);
        } catch (DataIntegrityViolationException e) {
            logger.error("Database error while creating permission: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating permission: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PermissionEntity> listAllPermissions() {
        try {
            Iterable<PermissionEntity> permissionIterable = permissionRepository.findAll();
            return StreamSupport.stream(permissionIterable.spliterator(), false)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Database error while listing all permissions: {}", e.getMessage());
            throw new CustomAuthenticationException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while listing all permissions: {}", e.getMessage());
            throw new CustomAuthenticationException("Unexpected error: " + e.getMessage(), e);
        }
    }


    @Override
    public ResponseEntity<?> updatePermission(PermissionDTO permissionDTO) {
        try {
            // Retrieve the existing permission entity from the database
            PermissionEntity existingPermission = permissionRepository.findById(permissionDTO.getId())
                    .orElseThrow(() -> new CustomAuthenticationException("Permission not found with id: " + permissionDTO.getId(), new Throwable()));

            // Update the existing permission entity with the new data from permissionDTO
            existingPermission.setPermissionName(permissionDTO.getPermissionName());
            existingPermission.setPermissionDescription(permissionDTO.getPermissionDescription());

            // Save the updated permission entity
            existingPermission = permissionRepository.save(existingPermission);

            // Convert the updated entity to DTO
            PermissionDTO updatedPermissionDTO = new PermissionDTO();
            updatedPermissionDTO.setId(existingPermission.getTId());
            updatedPermissionDTO.setPermissionName(existingPermission.getPermissionName());
            updatedPermissionDTO.setPermissionDescription(existingPermission.getPermissionDescription());

            // Log the updated permission
            logger.info("Permission updated: {}", updatedPermissionDTO);

            // Create response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Permission updated successfully.");

            // Create response DTO
            PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();
            permissionResponseDTO.setPermissionDTO(updatedPermissionDTO);
            permissionResponseDTO.setResponseStatus(responseStatus);

            // Return the response entity
            return ResponseEntity.ok().body(permissionResponseDTO);
        } catch (CustomAuthenticationException e) {
            logger.error("Error while updating permission: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while updating permission: {}", e.getMessage());
            throw new CustomAuthenticationException("Failed to update permission: " + e.getMessage(), e);
        }
    }

    @Override
    public PermissionEntity getPermissionById(Long id) {
        try {
            // Retrieve the permission entity from the database by ID
            return permissionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));
        } catch (EntityNotFoundException e) {
            // Log the error or rethrow to let the caller handle it
            throw e;
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            // Rethrow a more generic exception or handle as appropriate
            throw new CustomAuthenticationException("Failed to get permission: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> deletePermissionById(Long id) {
        try {
            // Retrieve the permission entity from the database by ID
            Optional<PermissionEntity> optionalPermission = Optional.ofNullable(permissionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id)));

            // Permission found, delete it from the repository
            PermissionEntity permissionToDelete = optionalPermission.get();
            permissionRepository.delete(permissionToDelete);

            // Construct the response DTO
            PermissionResponseDTO responseDTO = new PermissionResponseDTO();

            // Construct the response status
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setResponseCode("200");
            responseStatus.setResponseDesc("Permission deleted successfully.");
            responseDTO.setResponseStatus(responseStatus);

            // Construct the permissionDTO
            PermissionDTO permissionDTO = new PermissionDTO();
            permissionDTO.setId(permissionToDelete.getTId());
            permissionDTO.setPermissionName(permissionToDelete.getPermissionName());
            permissionDTO.setPermissionDescription(permissionToDelete.getPermissionDescription());
            responseDTO.setPermissionDTO(permissionDTO);

            // Return the response entity with the DTO
            return ResponseEntity.ok().body(responseDTO);
        } catch (CustomAuthenticationException e) {
            logger.error("Error while deleting permission: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while deleting permission: {}", e.getMessage());
            throw new CustomAuthenticationException("Failed to delete permission: " + e.getMessage(), e);
        }
    }
}
