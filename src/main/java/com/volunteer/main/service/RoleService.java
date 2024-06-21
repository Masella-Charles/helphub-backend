package com.volunteer.main.service;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.model.request.RoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface RoleService {
    ResponseEntity<?> createRole(RoleDTO roleDTO);
    List<RoleEntity> listAllRoles();
    ResponseEntity <?> updateRole(RoleDTO roleDTO);
    RoleEntity getRoleById(Long id);
    ResponseEntity<?> deleteRoleById(Long id);
}
