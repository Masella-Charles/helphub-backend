package com.volunteer.main.model.response;

import com.volunteer.main.model.request.PermissionDTO;
import com.volunteer.main.model.request.RoleDTO;
import lombok.Data;

@Data
public class RoleResponseDTO {
    protected ResponseStatus responseStatus;
    protected RoleDTO roleDTO;
}
