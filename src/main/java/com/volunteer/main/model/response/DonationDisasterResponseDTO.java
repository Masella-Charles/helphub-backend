package com.volunteer.main.model.response;


import lombok.Data;

import java.time.LocalDate;

@Data
public class DonationDisasterResponseDTO {
    private ResponseStatus responseStatus;
    private Long Id;
    private String donorName;
    private String type; // 'money' or 'commodity'
    private Double amount;
    private String commodityName;
    private Integer quantity;
    private Boolean status;
    private DisasterEntity disasterEntity; // Reference to DisasterEntity

    @Data
    public static class DisasterEntity{

        private Long tId;
        private String name;
        private String description;
        private LocalDate date;
        private Boolean status;

    }

}
