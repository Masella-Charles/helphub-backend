package com.volunteer.main.model.response;

import com.volunteer.main.model.request.TestimonialDTO;
import lombok.Data;

@Data
public class TestimonialResponseDTO {
    private ResponseStatus responseStatus;
    private TestimonialDTO testimonialDTO;
}
