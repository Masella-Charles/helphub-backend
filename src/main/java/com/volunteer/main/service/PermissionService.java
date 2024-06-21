package com.volunteer.main.service;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.model.request.PermissionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    ResponseEntity<?> createPermission(PermissionDTO permissionDTO);
    List<PermissionEntity> listAllPermissions();
    ResponseEntity <?> updatePermission(PermissionDTO permissionDTO);
    PermissionEntity getPermissionById(Long id);
    ResponseEntity<?> deletePermissionById(Long id);
}
