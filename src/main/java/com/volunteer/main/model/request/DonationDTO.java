package com.volunteer.main.model.request;

import lombok.Data;

@Data
public class DonationDTO {
    private Long Id;
    private String donorName;
    private String type; // 'money' or 'commodity'
    private Double amount;
    private String commodityName;
    private Integer quantity;
    private Boolean status;
    private Long disasterId; // Reference to DisasterEntity

}
