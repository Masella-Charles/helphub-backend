package com.volunteer.main.model.response;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimeSheetResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long opportunityId;
    private String opportunityName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
