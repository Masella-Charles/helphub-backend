package com.volunteer.main.model.response;


import com.volunteer.main.model.request.PermissionDTO;
import lombok.Data;

@Data
public class PermissionResponseDTO {
    protected ResponseStatus responseStatus;
    protected PermissionDTO permissionDTO;
}
