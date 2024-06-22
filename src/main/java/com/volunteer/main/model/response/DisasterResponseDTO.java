package com.volunteer.main.model.response;

import com.volunteer.main.model.request.DisasterDTO;
import lombok.Data;

@Data
public class DisasterResponseDTO {
    protected ResponseStatus responseStatus;
    protected DisasterDTO disasterDTO;
}
