package com.volunteer.main.model.request;


import com.volunteer.main.entity.RoleEntity;
import lombok.Data;

import java.util.Set;

@Data
public class PermissionDTO {
    private Long id;
    private String permissionName;
    private String permissionDescription;
}
