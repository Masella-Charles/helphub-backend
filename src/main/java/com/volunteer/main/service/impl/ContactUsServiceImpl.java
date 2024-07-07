package com.volunteer.main.service.impl;


import com.volunteer.main.entity.ContactUsEntity;
import com.volunteer.main.model.request.ContactUsRequestDTO;
import com.volunteer.main.model.response.ContactUsResponseDTO;
import com.volunteer.main.model.response.ResponseStatus;
import com.volunteer.main.repositories.ContactUsRepository;
import com.volunteer.main.service.ContactUsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository contactUsRepository;

    public ContactUsServiceImpl(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    @Override
    public ContactUsResponseDTO createContactUs(ContactUsRequestDTO contactUsRequestDTO) {
        ContactUsEntity contactUsEntity = new ContactUsEntity();
        contactUsEntity.setName(contactUsRequestDTO.getName());
        contactUsEntity.setEmail(contactUsRequestDTO.getEmail());
        contactUsEntity.setMessage(contactUsRequestDTO.getMessage());
        contactUsEntity.setCreatedAt(LocalDateTime.now());
        contactUsEntity.setStage(null);
        contactUsEntity = contactUsRepository.save(contactUsEntity);
        return mapToResponseDTO(contactUsEntity);
    }

    @Override
    public ContactUsResponseDTO getContactUsById(Long id) {
        ContactUsEntity contactUsEntity = contactUsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact Us entry not found with id: " + id));
        return mapToResponseDTO(contactUsEntity);
    }

    @Override
    public List<ContactUsResponseDTO> getAllContactUs() {
        Iterable<ContactUsEntity> entities = contactUsRepository.findAll();
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContactUsResponseDTO updateContactUs(Long id, ContactUsRequestDTO contactUsRequestDTO) {
        ContactUsEntity contactUsEntity = contactUsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact Us entry not found with id: " + id));
        contactUsEntity.setName(contactUsRequestDTO.getName());
        contactUsEntity.setEmail(contactUsRequestDTO.getEmail());
        contactUsEntity.setMessage(contactUsRequestDTO.getMessage());
        contactUsEntity.setStage(contactUsRequestDTO.getStage());
        contactUsEntity = contactUsRepository.save(contactUsEntity);
        return mapToResponseDTO(contactUsEntity);
    }

    @Override
    public void deleteContactUs(Long id) {
        ContactUsEntity contactUsEntity = contactUsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact Us entry not found with id: " + id));
        contactUsRepository.delete(contactUsEntity);
    }

    private ContactUsResponseDTO mapToResponseDTO(ContactUsEntity contactUsEntity) {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setResponseDesc("Success");
        responseStatus.setResponseCode("200");
        ContactUsResponseDTO responseDTO = new ContactUsResponseDTO();
        ContactUsRequestDTO contactUsRequestDTO = new ContactUsRequestDTO();
        contactUsRequestDTO.setId(contactUsEntity.getId());
        contactUsRequestDTO.setName(contactUsEntity.getName());
        contactUsRequestDTO.setEmail(contactUsEntity.getEmail());
        contactUsRequestDTO.setMessage(contactUsEntity.getMessage());
        contactUsRequestDTO.setCreatedAt(contactUsEntity.getCreatedAt());
        contactUsRequestDTO.setStage(contactUsEntity.getStage());

        responseDTO.setContactUsRequestDTO(contactUsRequestDTO);
        responseDTO.setResponseStatus(responseStatus);
        return responseDTO;
    }
}