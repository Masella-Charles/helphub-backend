package com.volunteer.main.controller;


import com.volunteer.main.model.request.OpportunityImageDTO;
import com.volunteer.main.service.OpportunityImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/opportunityImage")
@CrossOrigin(origins = "*")
public class OpportunityImageController {

    private final OpportunityImageService opportunityImageService;

    public OpportunityImageController(OpportunityImageService opportunityImageService) {
        this.opportunityImageService = opportunityImageService;
    }

    @PostMapping("/create")
    public ResponseEntity<OpportunityImageDTO> createImage(@RequestBody OpportunityImageDTO opportunityImageDTO) {
        OpportunityImageDTO createdImage = opportunityImageService.createImage(opportunityImageDTO);
        return ResponseEntity.ok(createdImage);
    }

    @PutMapping("/update")
    public ResponseEntity<OpportunityImageDTO> updateImage(@RequestBody OpportunityImageDTO opportunityImageDTO) {
        OpportunityImageDTO updatedImage = opportunityImageService.updateImage(opportunityImageDTO);
        return ResponseEntity.ok(updatedImage);
    }

    @GetMapping("/getByOpportunityId")
    public ResponseEntity<OpportunityImageDTO> getImageByOpportunityId(@RequestParam Long opportunityId) {
        OpportunityImageDTO imageDTO = opportunityImageService.getImageByOpportunityId(opportunityId);
        return ResponseEntity.ok(imageDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        opportunityImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
