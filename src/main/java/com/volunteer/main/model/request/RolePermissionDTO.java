package com.volunteer.main.model.request;

import com.volunteer.main.entity.PermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RolePermissionDTO {
    private Long roleId;
    private String roleName;
    private Set<PermissionDTO> permissions;

    public RolePermissionDTO(Long roleId, String roleName, Set<PermissionEntity> permissionEntities) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissions = permissionEntities.stream()
                .map(permission -> new PermissionDTO(permission.getTId(), permission.getPermissionName(), permission.getPermissionDescription()))
                .collect(Collectors.toSet());
    }

    @Data
    public static class PermissionDTO {
        private Long permissionId;
        private String permissionName;
        private String permissionDescription;

        public PermissionDTO(Long permissionId, String permissionName, String permissionDescription) {
            this.permissionId = permissionId;
            this.permissionName = permissionName;
            this.permissionDescription = permissionDescription;
        }
    }
}
