package com.volunteer.main.model.response;


import com.volunteer.main.model.request.ContactUsRequestDTO;
import lombok.Data;

@Data
public class ContactUsResponseDTO {
    private ResponseStatus responseStatus;
    private ContactUsRequestDTO contactUsRequestDTO;
}
