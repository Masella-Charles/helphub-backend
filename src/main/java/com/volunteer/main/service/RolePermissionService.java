package com.volunteer.main.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RolePermissionService {
    ResponseEntity<?> addPermissionToRole(Long roleId, Long permissionId);
    ResponseEntity<?> removePermissionFromRole(Long roleId, Long permissionId);
    ResponseEntity<?> getPermissionsByRole(Long roleId);
    ResponseEntity<?> getAllRolePermissions();
    ResponseEntity<?> editRolePermissions(Long roleId, Set<Long> permissionIds);
}
