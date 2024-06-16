package com.volunteer.main.model.response;

import com.volunteer.main.model.request.VolunteerDto;
import lombok.Data;

@Data
public class VolunteerResponseDTO {
    protected ResponseStatus responseStatus;
    protected VolunteerDto volunteer;
}
