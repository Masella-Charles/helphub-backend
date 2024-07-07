package com.volunteer.main.service;

import com.volunteer.main.model.request.ContactUsRequestDTO;
import com.volunteer.main.model.response.ContactUsResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactUsService {
    ContactUsResponseDTO createContactUs(ContactUsRequestDTO contactUsRequestDTO);

    ContactUsResponseDTO getContactUsById(Long id);

    List<ContactUsResponseDTO> getAllContactUs();

    ContactUsResponseDTO updateContactUs(Long id, ContactUsRequestDTO contactUsRequestDTO);

    void deleteContactUs(Long id);
}
