package com.volunteer.main.model.request;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimeSheetDTO {
    private Long id;
    private Long userId;
    private Long opportunityId;
    private Boolean status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
