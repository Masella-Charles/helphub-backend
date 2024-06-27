package com.volunteer.main.model.request;

import lombok.Data;

@Data
public class DonationDistributionDTO {
    private Long id;
    private String recipientName;
    private Double amountDistributed;
    private Integer quantityDistributed;

    // DonationEntity fields
    private Long donationId;
    private String donorName;
    private String donationType; // 'money' or 'commodity'
    private Double donationAmount;
    private String commodityName;
    private Integer donationQuantity;
    private Boolean donationStatus;
}
