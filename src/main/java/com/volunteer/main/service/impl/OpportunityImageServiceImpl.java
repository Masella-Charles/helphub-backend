package com.volunteer.main.service.impl;

import com.volunteer.main.entity.OpportunityEntity;
import com.volunteer.main.entity.OpportunityImageEntity;
import com.volunteer.main.model.request.OpportunityImageDTO;
import com.volunteer.main.repositories.OpportunityImageRepository;
import com.volunteer.main.repositories.OpportunityRepository;
import com.volunteer.main.service.OpportunityImageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
public class OpportunityImageServiceImpl implements OpportunityImageService {

    private final OpportunityImageRepository opportunityImageRepository;


    private final OpportunityRepository opportunityRepository;

    public OpportunityImageServiceImpl(OpportunityImageRepository opportunityImageRepository, OpportunityRepository opportunityRepository) {
        this.opportunityImageRepository = opportunityImageRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public OpportunityImageDTO createImage(OpportunityImageDTO opportunityImageDTO) {
        OpportunityEntity opportunityEntity = opportunityRepository.findById(opportunityImageDTO.getOpportunityId())
                .orElseThrow(() -> new EntityNotFoundException("Opportunity not found with id: " + opportunityImageDTO.getOpportunityId()));

        OpportunityImageEntity imageEntity = new OpportunityImageEntity();
        imageEntity.setFileName(opportunityImageDTO.getFileName());

        String encodedString = Base64.getEncoder().encodeToString(opportunityImageDTO.getImageData().getBytes());

        imageEntity.setImageData(encodedString.getBytes());

        imageEntity.setOpportunityEntity(opportunityEntity);

        imageEntity = opportunityImageRepository.save(imageEntity);

        opportunityImageDTO.setId(imageEntity.getId());
        return opportunityImageDTO;
    }

    @Override
    public OpportunityImageDTO updateImage(OpportunityImageDTO opportunityImageDTO) {
        OpportunityImageEntity imageEntity = opportunityImageRepository.findById(opportunityImageDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + opportunityImageDTO.getId()));

        imageEntity.setFileName(opportunityImageDTO.getFileName());
        String encodedString = Base64.getEncoder().encodeToString(opportunityImageDTO.getImageData().getBytes());

        imageEntity.setImageData(encodedString.getBytes());

        imageEntity = opportunityImageRepository.save(imageEntity);

        return opportunityImageDTO;
    }

    @Override
    public OpportunityImageDTO getImageByOpportunityId(Long opportunityId) {
        OpportunityImageEntity imageEntity = opportunityImageRepository.findByOpportunityEntity_Id(opportunityId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found for opportunity with id: " + opportunityId));

        OpportunityImageDTO dto = new OpportunityImageDTO();
        dto.setId(imageEntity.getId());
        dto.setFileName(imageEntity.getFileName());
        dto.setImageData(Arrays.toString(imageEntity.getImageData()));
        dto.setOpportunityId(opportunityId);

        return dto;
    }

    @Override
    public void deleteImage(Long id) {
        OpportunityImageEntity imageEntity = opportunityImageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + id));
        opportunityImageRepository.delete(imageEntity);
    }
}