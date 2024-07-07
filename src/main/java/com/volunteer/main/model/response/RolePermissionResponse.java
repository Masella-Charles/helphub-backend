package com.volunteer.main.model.response;

import lombok.Data;

import java.util.Set;

@Data
public class RolePermissionResponse {
    private Long roleId;
    private String roleName;
    private Set<PermissionResponseDTO> permissions;
}
