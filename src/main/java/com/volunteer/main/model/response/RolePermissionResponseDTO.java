package com.volunteer.main.model.response;


import com.volunteer.main.model.request.RolePermissionDTO;
import lombok.Data;

@Data
public class RolePermissionResponseDTO {
    protected ResponseStatus responseStatus;
    protected RolePermissionDTO rolePermissionDTO;
}
