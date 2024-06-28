package com.volunteer.main.model.request;


import lombok.Data;

@Data
public class RolePermissionRequest {
    private Long roleId;
    private Long permissionId;
}
